package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Camera;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private Rectangle border;
    private List<Rectangle> squares;
    private List<Circle> circles;
    private List<Polygon> triangles;
    private ShapeRenderer shapeRenderer;
    private Random random;

    public GameMap() {
        // Đường biên map
        border = new Rectangle(0, 0, 200, 150);

        // Danh sách vật thể
        squares = new ArrayList<>();
        circles = new ArrayList<>();
        triangles = new ArrayList<>();
        random = new Random();

        // Tạo ngẫu nhiên 1000 vật thể
        for (int i = 0; i < 1000; i++) {
            int shapeType = random.nextInt(3) + 1;

            float x = 5 + random.nextFloat() * (border.width - 10);
            float y = 5 + random.nextFloat() * (border.height - 10);

            switch (shapeType) {
                case 1: // Vuông
                    float size = 0.5f + random.nextFloat() * 19.5f; // 0.5 -> 20
                    squares.add(new Rectangle(x, y, size, size));
                    break;

                case 2: // Tròn
                    float radius = 0.3f + random.nextFloat() * 14.7f; // 0.3 -> 15
                    circles.add(new Circle(x, y, radius));
                    break;

                case 3: // Tam giác
                    float triangleSize = 1f + random.nextFloat() * 24f; // 1 -> 25
                    float[] vertices = {
                        x, y,
                        x + triangleSize, y,
                        x + triangleSize / 2f, y + triangleSize
                    };
                    triangles.add(new Polygon(vertices));
                    break;
            }
        }

        shapeRenderer = new ShapeRenderer();
    }

    public void render(Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(border.x, border.y, border.width, border.height);

        shapeRenderer.setColor(Color.GREEN);
        for (Rectangle square : squares) {
            shapeRenderer.rect(square.x, square.y, square.width, square.height);
        }

        shapeRenderer.setColor(Color.BLUE);
        for (Circle circle : circles) {
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }

        shapeRenderer.setColor(Color.YELLOW);
        for (Polygon triangle : triangles) {
            float[] v = triangle.getVertices();
            shapeRenderer.triangle(v[0], v[1], v[2], v[3], v[4], v[5]);
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
