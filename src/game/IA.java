package game;

import javax.swing.JFrame;

public class IA {
    /**
     * Random intelligent agent.
     *
     * @param args
     */
    public static void main(String[] args) {

        for (int i = 0; i < 1; i++) {
            new Thread() {
                @Override
                public void run() {
                    JFrame f = new JFrame("Tetris");
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    f.setSize(12 * 26 + 10, 26 * 23 + 25);
                    f.setVisible(true);
                    final Tetris game = new Tetris();
                    game.init();
                    f.add(game);
                    int times = 2000;
                    while (times > 0) {
                        try {

                            //
                            Thread.sleep(250);
                            // Insert here calls to IA methods
                            if (movido==false) {
                                int conta=  game.rotalo(game.imagine()[0],game.imagine()[1]);
                                play(game);
                                game.corregir(conta);

                            }
                            game.dropDown();
                        } catch (InterruptedException e) {
                        }
                        times--;
                    }
                    System.out.println("Score :" + game.getScore());
                }
            }.start();
        }
    }


    /**
     * Decides to move or not.
     *
     * @param game
     */
    public static boolean movido=false;
    protected static void play(Tetris game) {
        int[] sol = game.imagine();


            if (game.getPos() > sol[0]) {
                game.move(-1);
            }
            if (game.getPos() < sol[0]) {
                    game.move(1);
                }
            if(game.getPos() == sol[0]) {
                    game.move(0);
                    movido=true;


                }


            }





    }

