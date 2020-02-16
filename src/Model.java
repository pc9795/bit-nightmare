import utils.GameObject;
import utils.Point3f;
import utils.Vector3f;

import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   /
   (MIT LICENSE ) e.g do what you want with this :-) 
 */
public class Model {
    private GameObject player;
    private Controller controller = Controller.getInstance();
    private CopyOnWriteArrayList<GameObject> enemiesList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<GameObject> bulletList = new CopyOnWriteArrayList<>();
    private int score = 0;

    public Model() {
        //setup game world
        player = new GameObject("res/Lightning.png", 50, 50, new Point3f(500, 500, 0));

        //Enemies  starting with four
        enemiesList.add(new GameObject("res/UFO.png", 50, 50, new Point3f(((float) Math.random() * 50 + 400), 0, 0)));
        enemiesList.add(new GameObject("res/UFO.png", 50, 50, new Point3f(((float) Math.random() * 50 + 500), 0, 0)));
        enemiesList.add(new GameObject("res/UFO.png", 50, 50, new Point3f(((float) Math.random() * 100 + 500), 0, 0)));
        enemiesList.add(new GameObject("res/UFO.png", 50, 50, new Point3f(((float) Math.random() * 100 + 400), 0, 0)));
    }

    /**
     * NOTE: This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly.
     */
    void gameLogic() {
        // Player Logic first
        playerLogic();
        // Enemy Logic next
        enemyLogic();
        // Bullets move next
        bulletLogic();
        // interactions between objects
        gameLogicUtil();

    }

    private void gameLogicUtil() {
        // this is a way to increment across the array list data structure
        // see if they hit anything
        // using enhanced for-loop style as it makes it alot easier both code wise and reading wise too
        for (GameObject temp : enemiesList) {
            for (GameObject Bullet : bulletList) {
                if (Math.abs(temp.getCentre().getX() - Bullet.getCentre().getX()) < temp.getWidth()
                        && Math.abs(temp.getCentre().getY() - Bullet.getCentre().getY()) < temp.getHeight()) {
                    enemiesList.remove(temp);
                    bulletList.remove(Bullet);
                    score++;
                }
            }
        }

    }

    private void enemyLogic() {
        // TODO Auto-generated method stub
        for (GameObject temp : enemiesList) {
            // Move enemies

            temp.getCentre().applyVector(new Vector3f(0, -1, 0));


            //see if they get to the top of the screen ( remember 0 is the top
            if (temp.getCentre().getY() == 900.0f)  // current boundary need to pass value to model
            {
                enemiesList.remove(temp);

                // enemies win so score decreased
                score--;
            }
        }

        if (enemiesList.size() < 2) {
            while (enemiesList.size() < 6) {
                enemiesList.add(new GameObject("res/UFO.png", 50, 50, new Point3f(((float) Math.random() * 1000), 0, 0)));
            }
        }
    }

    private void bulletLogic() {
        // TODO Auto-generated method stub
        // move bullets

        for (GameObject temp : bulletList) {
            //check to move them

            temp.getCentre().applyVector(new Vector3f(0, 1, 0));
            //see if they hit anything

            //see if they get to the top of the screen ( remember 0 is the top
            if (temp.getCentre().getY() == 0) {
                bulletList.remove(temp);
            }
        }

    }

    private void playerLogic() {

        // smoother animation is possible if we make a target position  // done but may try to change things for students

        //check for movement and if you fired a bullet

        if (Controller.getInstance().isKeyAPressed()) {
            player.getCentre().applyVector(new Vector3f(-2, 0, 0));
        }

        if (Controller.getInstance().isKeyDPressed()) {
            player.getCentre().applyVector(new Vector3f(2, 0, 0));
        }

        if (Controller.getInstance().isKeyWPressed()) {
            player.getCentre().applyVector(new Vector3f(0, 2, 0));
        }

        if (Controller.getInstance().isKeySPressed()) {
            player.getCentre().applyVector(new Vector3f(0, -2, 0));
        }

        if (Controller.getInstance().isKeySpacePressed()) {
            CreateBullet();
            Controller.getInstance().setKeySpacePressed(false);
        }

    }

    private void CreateBullet() {
        bulletList.add(new GameObject("res/Bullet.png", 32, 64, new Point3f(player.getCentre().getX(), player.getCentre().getY(), 0.0f)));

    }

    public GameObject getPlayer() {
        return player;
    }

    public CopyOnWriteArrayList<GameObject> getEnemies() {
        return enemiesList;
    }

    public CopyOnWriteArrayList<GameObject> getBullets() {
        return bulletList;
    }

    public int getScore() {
        return score;
    }


}


