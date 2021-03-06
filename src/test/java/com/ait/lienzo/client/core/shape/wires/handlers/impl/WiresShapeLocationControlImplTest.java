package com.ait.lienzo.client.core.shape.wires.handlers.impl;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.test.LienzoMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(LienzoMockitoTestRunner.class)
public class WiresShapeLocationControlImplTest {

    private static final double START_X = 3;
    private static final double START_Y = 5;

    private WiresShapeLocationControlImpl tested;
    private WiresShape shape;

    @Before
    public void setup() {
        shape = new WiresShape(new MultiPath().rect(0, 0, 10, 10));
        this.tested = new WiresShapeLocationControlImpl(shape);
        assertEquals(shape, tested.getShape());
    }

    @Test
    public void testOnMoveStart() {
        tested.onMoveStart(START_X, START_Y);
        assertEquals(START_X, tested.getMouseStartX(), 0);
        assertEquals(START_Y, tested.getMouseStartY(), 0);
        assertEquals(5, tested.getShapeStartCenterX(), 0);
        assertEquals(5, tested.getShapeStartCenterY(), 0);
        assertEquals(new Point2D(0, 0), tested.getShapeInitialLocation());
        assertEquals(new Point2D(0, 0), tested.getAdjust());
        assertEquals(new Point2D(START_X, START_Y), tested.getCurrentLocation());
        assertEquals(new Point2D(0, 0), tested.getCurrentDelta());
        assertFalse(tested.isStartDocked());
    }

    @Test
    public void testOnMove() {
        final double x = 1.5;
        final double y = 2.2;
        tested.onMoveStart(START_X, START_Y);
        tested.onMove(x, y);
        assertEquals(new Point2D(0, 0), tested.getAdjust());
        assertEquals(new Point2D(START_X + x, START_Y + y), tested.getCurrentLocation());
        assertEquals(new Point2D(x, y), tested.getCurrentDelta());
    }

    @Test
    public void testOnMoveComplete() {
        tested.onMoveStart(START_X, START_Y);
        tested.onMove(1, 2);
        assertTrue(tested.onMoveComplete());
    }

    @Test
    public void testOnMoveAdjusted() {
        tested.onMoveStart(START_X, START_Y);
        tested.onMove(1, 2);
        assertEquals(new Point2D(1, 2), tested.getCurrentDelta());
        assertEquals(new Point2D(START_X + 1, START_Y + 2), tested.getCurrentLocation());
        tested.onMoveAdjusted(new Point2D(6, 22));
        assertEquals(new Point2D(6, 22), tested.getCurrentDelta());
        assertEquals(new Point2D(START_X + 6, START_Y + 22), tested.getCurrentLocation());
    }

    @Test
    public void testClear() {
        tested.onMoveStart(START_X, START_Y);
        tested.onMove(1, 2);
        tested.clear();
        assertEquals(0, tested.getMouseStartX(), 0);
        assertEquals(0, tested.getMouseStartY(), 0);
        assertEquals(0, tested.getShapeStartCenterX(), 0);
        assertEquals(0, tested.getShapeStartCenterY(), 0);
        assertEquals(new Point2D(0, 0), tested.getAdjust());
        assertEquals(new Point2D(0, 0), tested.getCurrentDelta());
        assertEquals(new Point2D(0, 0), tested.getCurrentLocation());
        assertNull(tested.getShapeInitialLocation());
    }

    @Test
    public void testExecute() {
        tested.onMoveStart(START_X, START_Y);
        tested.onMove(1, 2);
        tested.onMoveComplete();
        tested.execute();
        assertEquals(new Point2D(START_X + 1, START_Y + 2), tested.getCurrentLocation());
        assertEquals(new Point2D(START_X + 1, START_Y + 2), shape.getLocation());
    }

    @Test
    public void testReset() {
        tested.onMoveStart(START_X, START_Y);
        tested.onMove(1, 2);
        tested.onMoveComplete();
        assertEquals(new Point2D(START_X + 1, START_Y + 2), tested.getCurrentLocation());
        tested.reset();
        assertEquals(new Point2D(0, 0), shape.getLocation());
    }
}
