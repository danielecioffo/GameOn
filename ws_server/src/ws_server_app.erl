%%%-------------------------------------------------------------------
%% @doc ws_server public API
%% @end
%%%-------------------------------------------------------------------

-module(ws_server_app).

-behaviour(application).

-export([start/2, stop/1]).

start(_StartType, _StartArgs) ->
    Dispatch = cowboy_router:compile([
        {'_', [{"/ws", ws_handler, {} }]} %% {} initial state is an empty tuple
    ]),
    {ok, _} = cowboy:start_clear(http_listener,
        [{port, 8090}],
        #{env => #{dispatch => Dispatch}}
    ),
    OnlineUsersServer = spawn(online_users_server, init_server, []),
    register(online_users, OnlineUsersServer),
    io:format("===> Booted online_users_server, PID ~w ~n", [OnlineUsersServer]),
    ws_server_sup:start_link().

stop(_State) ->
    online_users ! {self(), {stop}},
    ok.

%% internal functions
