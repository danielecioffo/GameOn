%%%-------------------------------------------------------------------
%%% @author Federica Baldi, Francesco Campilongo, Daniele Cioffo
%%% @copyright (C) 2020, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 30. dic 2020 23:45
%%%-------------------------------------------------------------------
-module(ws_handler).
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
  if
    %% Registration of this process with the atom of the username, this is useful for contacting him from other processes
    Type == username_registration -> register(erlang:binary_to_atom(maps:get(<<"data">>, Map)), self());
    Type == game_request ->
      Receiver = erlang:binary_to_atom(maps:get(<<"receiver">>, Map)),
      Receiver ! Text; %% send the JSON structure to the receiver
    true -> ok
  end,
  {ok, State}.

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
terminate (TerminateReason, _Req, _State) ->
  io:format("Terminate reason: ~p\n", [TerminateReason]),
  ok.