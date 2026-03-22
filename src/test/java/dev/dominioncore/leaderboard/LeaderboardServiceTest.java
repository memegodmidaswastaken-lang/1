package dev.dominioncore.leaderboard;

public final class LeaderboardServiceTest {
    private LeaderboardServiceTest() {}

    public static void runAll() {
        LeaderboardService board = new LeaderboardService();
        board.recordAuthority("f1", 120.0);
        board.recordAuthority("f2", 220.0);
        board.recordFaith("r1", 90.0);
        board.recordFaith("r2", 140.0);
        board.addKills("p1", 5);
        board.addKills("p1", 2);
        board.addKills("p2", 10);

        check(board.topAuthority(1).get(0).id().equals("f2"), "Authority ranking mismatch");
        check(board.topFaith(1).get(0).id().equals("r2"), "Faith ranking mismatch");
        check(board.topKills(1).get(0).id().equals("p2"), "Kill ranking mismatch");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
