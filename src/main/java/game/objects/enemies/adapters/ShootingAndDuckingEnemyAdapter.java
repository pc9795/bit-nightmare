package game.objects.enemies.adapters;

import game.framework.Model;
import game.framework.visual.Animator;
import game.properties.Animated;
import game.physics.Point2f;

/**
 * Created By: Prashant Chaubey
 * Created On: 24-02-2020 01:59
 * Purpose: This object will shoot and duck
 **/
public class ShootingAndDuckingEnemyAdapter extends ShootingEnemyAdapter implements Animated {
    protected boolean ducking;
    private Animator duckLeft, duckRight;

    public ShootingAndDuckingEnemyAdapter(int width, int height, Point2f centre, GameObjectType type) {
        super(width, height, centre, type);
    }

    @Override
    public void setupAnimator() {
        if (!isTextured()) {
            return;
        }
        //Setup animators provided by parent.
        super.setupAnimator();
        //FRAME GAP IS DEPENDENT ON THE IMAGES USED
        duckRight = new Animator(10, false, texture.getDuckRight());
        duckLeft = new Animator(10, false, texture.getDuckLeft());
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected Animator getAnimatorAccordingToState() {
        Animator animator = super.getAnimatorAccordingToState();
        switch (facingDirection) {
            case LEFT:
                if (!dead && ducking) {
                    animator = duckLeft;
                }
                break;
            case RIGHT:
                if (!dead && ducking) {
                    animator = duckRight;
                }
                break;
        }
        return animator;
    }

    @Override
    public void perceiveEnv(Model model) {
        if (health <= 0) {
            if (!dead) {
                //Death animation should start from standing position
                if (ducking) {
                    toggleDuck();
                }
                dead = true;
                deathTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - deathTime > CORPSE_REMOVAL_PERIOD_IN_SEC * 1000) {
                model.getEnemies().remove(this);
            }
            return;
        }
        //Detect player and attack
        attackPlayer(model);
        //Check collision
        falling = enemyCollision(this, model);
    }

    /**
     * toggle ducking state
     */
    protected void toggleDuck() {
        if (ducking) {
            //We are subtracting height as now height is itself height/2.
            centre.setY(centre.getY() - height);
            height *= 2;
            ducking = false;
        } else {
            centre.setY(centre.getY() + (height / 2));
            height /= 2;
            ducking = true;
        }
    }
}
