%%%-------------------------------------------------------------------
%%% @author Federica Baldi, Francesco Campilongo, Daniele Cioffo
%%% @copyright (C) 2020, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 30. dic 2020 23:45
%%%-------------------------------------------------------------------
-module(web_socket_handler).
-author("danyc").

%% API
-export([init/2, websocket_init/1, websocket_handle/2, websocket_info/2, terminate/3]).

%% In the init/2 we upgrade HTTP to WebSocket
%% In websocket_init/1 we put the initialization code that has to be performed after the upgrade
%% Note: CowBoy uses different processes for handling the connection and the requests, so they will have different PID.
%% Indeed, we will have one process that handles the requests for each client
init (Req, State) ->
  io:format("Inside the init/2 callback.\n", []),
  {cowboy_websocket, Req, State,
    %% No timeout, probably is better to insert a very long value, but not infinity, to handle the disconnection of the client
    #{idle_timeout => infinity}
  }.

%% All websocket callback returns the tuple {[Frame], State}, where the first element is a list of frame
%% this list of frame will be returned to the client
%% It is also possible not to return anything, with {ok, State}
%% The most used frame type are text and binary.
websocket_init (State) ->
  io:format("Inside the websocket_init callback.\n", []),
  io:format("PID of Websocket server is ~p. ~n", [self()]),
  {ok, State}.

%% The websocket_handle/2 callback is called when cowboy receive a frame from the client
websocket_handle ({text, Text}, State) ->
  io:format("Frame received: ~p\n", [Text]),
  Map = jsx:decode(Text, [return_maps]), %% Every message is a JSON text
  %% JSX work with binary data!
  Type = erlang:binary_to_atom(maps:get(<<"type">>, Map)), %% << ... >> is the syntax for bit string, we cast it into an atom
  Sender = erlang:binary_to_atom(maps:get(<<"sender">>, Map)),
  if
    %% Registration of this process with the atom of the username, this is useful for contacting him from other processes
    Type == username_registration ->
      SenderID = whereis(Sender),
      if
        SenderID =/= undefined -> %%user already logged in
          Response = jsx:encode(#{<<"code">> => 1, <<"type">> => <<"sender_already_logged">>, <<"data">> => <<>>,
            <<"sender">> => <<>>, <<"receiver">> => <<>>}),
          NewState = State,
          self() ! Response;
        true ->
          register(erlang:binary_to_atom(maps:get(<<"data">>, Map)), self()),
          NewState = State
      end;
    Type == online_list_registration ->
      Info = process_info(self(), registered_name),
      if
        Info == [] -> %%This scenario has not been implemented client side, no needed.
          Response = jsx:encode(#{<<"code">> => 1, <<"type">> => <<"sender_already_in_list">>, <<"data">> => <<>>,
            <<"sender">> => <<>>, <<"receiver">> => <<>>}),
          NewState = State,
          self() ! Response;
        true ->
          Name = element(2, erlang:process_info(self(), registered_name)),
          Game = erlang:binary_to_atom(maps:get(<<"data">>, Map)),
          online_users ! {Name, {Game, add}},
          NewState = {game_name, Game}
      end;
    Type == opponent_registration ->
      NewState = {opponent_username, erlang:binary_to_atom(maps:get(<<"data">>, Map))}; %% register in the state the opponent
    true ->
      NewState = State,
      Receiver = erlang:binary_to_atom(maps:get(<<"receiver">>, Map)),
      ReceiverPID = whereis(Receiver),
      if
        ReceiverPID == undefined -> %% The receiver is disconnected
          Response = jsx:encode(#{<<"code">> => 1, <<"type">> => <<"receiver_not_reachable">>, <<"data">> => <<>>,
            <<"sender">> => <<>>, <<"receiver">> => <<>>}),
          Sender ! Response;
        true -> %% No problem
          Receiver ! Text %% send the JSON structure to the receiver
      end
  end,
  {ok, NewState}.

%% The websocket_info/2 callback is called when we use the ! operator
%% So Cowboy will call websocket_info/2 whenever an Erlang message arrives.
websocket_info (stop, State) ->
  {stop, State}; %% Say to the server to terminate the connection

%% This function can be used to send a message Info to this process by another process
websocket_info(Info, State) ->
  {[{text, Info}], State}. %% Returns this message to the client

%% terminate/3 is for handling the termination of the connection
%% The first argument is the reason for the closing
%% The most common reasons are stop and remote
%% stop -> The server close the connection
%% remote -> The client close the connection

%% In case of termination in a game page
terminate (TerminateReason, _Req, {opponent_username, OpponentUsername}) ->
  io:format("Terminate reason: ~p\n", [TerminateReason]),
  OpponentPID = whereis(OpponentUsername),
  if
    OpponentPID == undefined -> %% The opponent is already disconnected
      ok;
    true -> %% No problem
      OpponentPID ! jsx:encode(#{<<"code">> => 0, <<"type">> => <<"opponent_disconnected">>, <<"data">> => <<>>,
        <<"sender">> => <<>>, <<"receiver">> => <<>>})
  end;

%% In case of termination in a waiting room
terminate (TerminateReason, _Req, {game_name, Game}) ->
  Name = element(2, erlang:process_info(self(), registered_name)),
  online_users ! {Name, {Game, remove}},
  io:format("Terminate reason: ~p\n", [TerminateReason]),
  ok;

terminate (TerminateReason, _Req, {}) ->
  io:format("Terminate reason: ~p\n", [TerminateReason]),
  io:format("Terminate with empty state").