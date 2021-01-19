%%%-------------------------------------------------------------------
%%% @author Federica Baldi, Francesco Campilongo, Daniele Cioffo
%%% @copyright (C) 2021, <COMPANY>
%%% @doc
%%% Server that handles the list of online users for all the games
%%% supported by GameOn platform
%%% @end
%%% Created : 18. gen 2021 15:12
%%%-------------------------------------------------------------------
-module(online_users_server).
-author("feder").

%% API
-export([init_server/0]).

% function to start the server
init_server() ->
  Server = self(),
  ConnectFour = spawn( fun() -> connect_four_loop(Server, []) end ),
  TicTacToe = spawn( fun() -> tic_tac_toe_loop(Server, []) end ),
  server_loop({ConnectFour, TicTacToe}).

%loop for the server node
server_loop(GameServers) ->
  receive
    {From, {connect_four, Operation} } ->
      element(1,GameServers) ! {From, Operation},
      server_loop(GameServers);
    {From, {tic_tac_toe, Operation} } ->
      element(2,GameServers) ! {From, Operation},
      server_loop(GameServers);
    {From, {stop} } ->
      element(1, GameServers) ! {self(), stop},
      element(2, GameServers) ! {self(), stop},
      From ! {ok};
    _ ->  %any other message is skipped
      server_loop(GameServers)
  end.


%loop for the connect_four process
connect_four_loop(Server, OnlineList) ->
  receive
    {From, add} ->
      NewOnlineList = OnlineList ++ [From],
      send_all(NewOnlineList),  %% the list of online users is updated for all online users
      io:format("Connect Four: ~s added to the list of online users~n", [From]),
      io:format("==> Connect Four currently online users: ~w ~n", [NewOnlineList]),
      connect_four_loop(Server, NewOnlineList);
    {From, remove} ->
      NewOnlineList = lists:delete(From, OnlineList),
      send_all(NewOnlineList),  %% the list of online users is updated for all online users
      send_all(NewOnlineList, From),  %% all game requests coming from this user are deleted
      io:format("Connect Four: ~s removed from the list of online users~n", [From]),
      io:format("==> Connect Four currently online users: ~w ~n", [NewOnlineList]),
      connect_four_loop(Server, NewOnlineList);
    {Server, stop} ->
      ok;  %no more actions, stop
    _ -> connect_four_loop(Server, OnlineList)  %any other message is skipped
  end.

% loop for the tic_tac_toe process
tic_tac_toe_loop(Server, OnlineList) ->
  receive
    {From, add} ->
      NewOnlineList = OnlineList ++ [From],
      send_all(NewOnlineList),  %% the list of online users is updated for all online users
      io:format("Tic-Tac-Toe: ~s added to the list of online users~n", [From]),
      io:format("==> Tic-Tac-Toe currently online users: ~w ~n", [NewOnlineList]),
      tic_tac_toe_loop(Server, NewOnlineList);
    {From, remove} ->
      NewOnlineList = lists:delete(From, OnlineList),
      send_all(NewOnlineList), %% the list of online users is updated for all online users
      send_all(NewOnlineList, From),  %% all game requests coming from this user are deleted
      io:format("Tic-Tac-Toe: ~s removed from the list of online users~n", [From]),
      io:format("==> Tic-Tac-Toe currently online users: ~w ~n", [NewOnlineList]),
      tic_tac_toe_loop(Server, NewOnlineList);
    {Server, stop} ->
      ok;  %no more actions, stop
    _ -> tic_tac_toe_loop(Server, OnlineList)  %any other message is skipped
  end.

% sends a message to all online users
send_all(List) ->
  send_all(List, List).

send_all([], _) -> ok;
send_all([First|Others], Data) ->
  if
    (is_list(Data)) ->  %% if it is a list, it means that the list of online users has been updated
      First ! jsx:encode(#{<<"code">> => 0, <<"type">> => <<"list_update">>, <<"data">> => Data, <<"sender">> => <<>>, <<"receiver">> => <<>>});
    true -> %% otherwise, it is the username of a user who is no longer online
      First ! jsx:encode(#{<<"code">> => 0, <<"type">> => <<"remove_requests">>, <<"data">> => Data, <<"sender">> => <<>>, <<"receiver">> => <<>>})
  end,
  send_all(Others, Data).