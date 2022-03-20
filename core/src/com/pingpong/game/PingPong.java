package com.pingpong.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


public class PingPong extends ApplicationAdapter {
	SpriteBatch batch;
	Texture racket_t;
	Texture ball_t;
	BitmapFont font;

	int screenWidth, screenHeight;
	int racketWidth = 210, racketHeight = 250;
	int ballWidth = 40, ballHeight = 40;
	int ballSpeedX = 500, ballSpeedY = 300;
	final int ballDashX = 50, ballDashY = 20;
	int score = 0;

	boolean start = false;

	Rectangle racket, racket2, ball;

	Sound hit;

	@Override
	public void create() {
		batch = new SpriteBatch();
		racket_t = new Texture("racket.png");
		ball_t = new Texture("ball.png");
		font = new BitmapFont();
		hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		racket = new Rectangle(100, screenHeight / 2 - racketHeight / 2,
				racketWidth, racketHeight);
		racket2 = new Rectangle(screenWidth - 100 - racketWidth,
				screenHeight / 2 - racketHeight / 2,
				racketWidth, racketHeight);
		ball = new Rectangle(screenWidth / 2 - ballWidth / 2,
				screenHeight / 2 - ballHeight / 2, ballWidth, ballHeight);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0.2f, 0.3f, 0.6f, 0);

		if (start) {
			if (Gdx.input.isTouched()) {
				racket.y = -Gdx.input.getY() - racketWidth / 2 + screenHeight;
			}
			racket2.y = ball.getY() - racketWidth / 2;

			ball.x -= ballSpeedX * Gdx.graphics.getDeltaTime();
			ball.y -= ballSpeedY * Gdx.graphics.getDeltaTime();
			if (ball.y + ballHeight > screenHeight) {
				ball.y = screenHeight - ballHeight;
				ballSpeedY = -ballSpeedY;
			} else if (ball.y < 0) {
				ball.y = 0;
				ballSpeedY = -ballSpeedY;
			}
			if (ball.x < 0 - ballWidth) {
				ball.x = screenWidth / 2 - ballWidth / 2;
				ball.y = screenHeight / 2 - ballHeight / 2;
				ballSpeedX = 500;
				ballSpeedY = 300;
				start = false;
			}
			if (racket.overlaps(ball) || racket2.overlaps(ball)) {
				hit.play();
				if (racket.overlaps(ball)) {
					ball.x = racket.getX() + racketWidth;
					score += 1;
				} else {
					ball.x = racket2.getX() - ballWidth;
				}
				if (ballSpeedX < 0) {
					ballSpeedX -= ballDashX;
				} else {
					ballSpeedX += ballDashX;
				}
				if (ballSpeedY < 0) {
					ballSpeedY -= ballDashY;
				} else {
					ballSpeedY += ballDashY;
				}
				ballSpeedX = -ballSpeedX;
			}
		} else {
			if (Gdx.input.justTouched()) {
				start = true;
				score = 0;
			}
		}

		batch.begin();
		batch.draw(racket_t, racket.getX(), racket.getY(), racket.getWidth(),
				racket.getHeight());
		batch.draw(racket_t, racket2.getX(), racket2.getY(), racket2.getWidth(),
				racket2.getHeight());
		font.draw(batch, Integer.toString(score), screenWidth / 2, screenHeight - 30);
		if (!start) {
			font.draw(batch, "Press to start", screenWidth / 2, screenHeight / 2);
		} else {
			batch.draw(ball_t, ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		racket_t.dispose();
		ball_t.dispose();
	}
}
