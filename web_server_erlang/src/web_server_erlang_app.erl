%%%-------------------------------------------------------------------
%% @doc ws_server public API
%% @end
%%%-------------------------------------------------------------------

-module(web_server_erlang_app).

-behaviour(application).

-export([start/2, stop/1]).

start(_StartType, _StartArgs) ->
    Dispatch = cowboy_router:compile([
        {'_', [{"/ws", handler, {} }]} %% {} initial state is an empty tuple
    ]),
    {ok, _} = cowboy:start_clear(http_listener,
        [{port, 8090}],
        #{env => #{dispatch => Dispatch}}
    ),
    OnlineUsersServer = spawn(online_users_server, init_server, []),
    register(online_users, OnlineUsersServer),
    io:format("===> Booted online_users_server, PID ~w ~n", [OnlineUsersServer]),
    web_server_erlang_sup:start_link().

stop(_State) ->
    online_users ! {self(), {stop}},
    ok.

%% internal functions
