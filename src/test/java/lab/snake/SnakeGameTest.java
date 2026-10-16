package lab.snake;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SnakeGameTest {
    @Test void normalMovePreservesLength() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);g.tick(Direction.RIGHT);
        assertEquals(new Cell(4,2),g.head());assertEquals(3,g.body().size());assertEquals(SnakeGame.Status.RUNNING,g.status());
    }
    @Test void foodGrowsSnakeAndAddsTen() {
        SnakeGame g=new SnakeGame(Scenario.PICNIC);g.tick(Direction.RIGHT);
        assertEquals(4,g.body().size());assertEquals(10,g.score());assertFalse(g.body().contains(g.food()));
    }
    @Test void reverseDirectionIsIgnored() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);g.tick(Direction.LEFT);
        assertEquals(new Cell(4,2),g.head());assertEquals(Direction.RIGHT,g.direction());
    }
    @Test void topWallEndsRound() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        g.tick(Direction.UP);g.tick(Direction.UP);g.tick(Direction.UP);
        assertEquals(SnakeGame.Status.WALL,g.status());assertEquals(new Cell(3,0),g.head());
    }
    @Test void endedRoundDoesNotMove() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        g.tick(Direction.UP);g.tick(Direction.UP);g.tick(Direction.UP);Cell head=g.head();
        g.tick(Direction.RIGHT);assertEquals(head,g.head());assertEquals(SnakeGame.Status.WALL,g.status());
    }
    @Test void restartRestoresStartingBody() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);g.tick(Direction.RIGHT);g.reset();
        assertEquals(Scenario.NORMAL.snake,g.body());assertEquals(Scenario.NORMAL.food,g.food());assertEquals(SnakeGame.Status.RUNNING,g.status());
    }
    @Test void loopMoveRightKeepsTailOrder() {
        SnakeGame g=new SnakeGame(Scenario.LOOP);g.tick(Direction.RIGHT);
        assertEquals(List.of(new Cell(3,2),new Cell(2,2),new Cell(1,2),new Cell(1,3)),g.body());
        assertEquals(new Cell(3,2),g.head());assertEquals(SnakeGame.Status.RUNNING,g.status());
    }
    @Test void loopMoveRightNoGrowth() {
        SnakeGame g=new SnakeGame(Scenario.LOOP);g.tick(Direction.RIGHT);
        assertEquals(4,g.body().size());assertEquals(0,g.score());
    }
    @Test void loopReverseMoveIgnored() {
        SnakeGame g=new SnakeGame(Scenario.LOOP);g.tick(Direction.DOWN);
        assertEquals(new Cell(2,1),g.head());assertEquals(Direction.UP,g.direction());
    }
    @Test void loopInitialBodyUnchanged() {
        SnakeGame g=new SnakeGame(Scenario.LOOP);
        assertEquals(Scenario.LOOP.snake,g.body());assertEquals(SnakeGame.Status.RUNNING,g.status());
    }
    @Test void selfCollisionEndsRound() {
        SnakeGame g=new SnakeGame(Scenario.LOOP);
        g.tick(Direction.RIGHT);g.tick(Direction.DOWN);g.tick(Direction.LEFT);g.tick(Direction.UP);
        assertEquals(SnakeGame.Status.SELF,g.status());
    }
    @Test void bottomWallEndsRound() {
        SnakeGame g=new SnakeGame(Scenario.LOOP);
        g.tick(Direction.RIGHT);g.tick(Direction.DOWN);g.tick(Direction.DOWN);g.tick(Direction.DOWN);g.tick(Direction.DOWN);
        assertEquals(SnakeGame.Status.WALL,g.status());
    }
    @Test void directionFromKeyValid() {
        assertEquals(Direction.UP,Direction.fromKey('w'));
        assertEquals(Direction.UP,Direction.fromKey('W'));
        assertEquals(Direction.DOWN,Direction.fromKey('S'));
        assertEquals(Direction.LEFT,Direction.fromKey('a'));
        assertEquals(Direction.RIGHT,Direction.fromKey('D'));
    }
    @Test void directionFromKeyInvalidThrows() {
        assertThrows(IllegalArgumentException.class,()->Direction.fromKey('x'));
    }
    @Test void constructorRejectsSmallBoard() {
        assertThrows(IllegalArgumentException.class,()->new SnakeGame(6,5,Scenario.NORMAL));
        assertThrows(IllegalArgumentException.class,()->new SnakeGame(8,4,Scenario.NORMAL));
    }
    @Test void renderContainsHeadAndFood() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        String out=g.render();
        assertTrue(out.contains("H"));assertTrue(out.contains("*"));assertTrue(out.startsWith("+--------+"));
    }
    @Test void snapshotReportsStatusAndLength() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        String s=g.snapshot();
        assertTrue(s.contains("status=RUNNING"));assertTrue(s.contains("length=3"));assertTrue(s.contains("score=0"));
    }
    @Test void accessorsExposeState() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        assertEquals(8,g.width());assertEquals(6,g.height());assertEquals(3,g.body().size());
        assertEquals(Direction.RIGHT,g.direction());assertEquals(new Cell(6,2),g.food());assertEquals(0,g.score());
        assertEquals(SnakeGame.Status.RUNNING,g.status());
    }
}
