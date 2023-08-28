import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Solve_GuessAttempts {

    public void TryGuess(String estate,Player playerGuessing){
        GuessBox gb = new GuessBox(estate,playerGuessing);

        gb.setLocation(400,300);
        gb.setSize(400,300);
        gb.setTitle("Guess");

        gb.pack();
        gb.setVisible(true);
    }

    public void TrySolve(Player playerSolving){
        SolveBox sb = new SolveBox(playerSolving);

        sb.setLocation(400,300);
        sb.setSize(400,300);
        sb.setTitle("Solve");

        sb.pack();
        sb.setVisible(true);
    }


    class GuessBox extends JDialog {

        JComboBox person;
        JComboBox weapon;
        JComboBox estate;

        Player guessing;
        GuessBox(String currentEstate, Player guessing){
            this.guessing = guessing;

            String[] people = {"Lucillia","Bert","Malina","Percy"};
            person = new JComboBox(people);
            String[] weapons = {"Broom","Scissors","Knife","Shovel","Shovel","IPad"};
            weapon = new JComboBox(weapons);
            //String[] estates = {"Haunted House","Manic Manor","Visitation Villa","Calamity Castle","Peril Palace"};
            String[] estates = {currentEstate};
            estate = new JComboBox(estates);

            JButton confirm = new JButton("Guess");
            confirm.addActionListener(e -> ConfirmPressed());

            this.setLayout(new GridBagLayout());

            JPanel selectBoxes = new JPanel();
            selectBoxes.add(person);
            selectBoxes.add(weapon);
            selectBoxes.add(estate);


            add(selectBoxes);
            add(confirm);

        }
        void ConfirmPressed(){

            Game g = Game.getGameInstance();

            HashMap<Player,Card> cards = new HashMap<Player,Card>();

            var players = g.getPlayerMap();
            for(int i : players.keySet()){
                if(guessing.equals(players.get(i))) continue;   //skips self
                for(Card c : players.get(i).getCards()){
                    if(containsCard(c)){
                        cards.put(players.get(i),c);
                    }
                }
            }

            //display
            getContentPane().removeAll();
            repaint();
            setLayout(new GridLayout(1,3));
            if(cards.size()==0)add(new JLabel("No other players have any of those Cards"));
            for(Player p : cards.keySet()){
                add(new JLabel(p.getName() + " has " + cards.get(p)));
            }
            revalidate();
            pack();

            boolean guess = false;


            Game.getGameInstance().guessMade(guess);
            GameController.updateView();
        }

        boolean containsCard(Card c){
            Game g = Game.getGameInstance();

            return(c.equals(g.getCardByName((String)weapon.getSelectedItem())) ||
                    c.equals(g.getCardByName((String)estate.getSelectedItem())) ||
                            c.equals(g.getCardByName((String)person.getSelectedItem())));


        }
    }


    class SolveBox extends JDialog {

        JComboBox person;
        JComboBox weapon;
        JComboBox estate;
        Player guessing;
        SolveBox(Player guessing){


            this.guessing = guessing;

            String[] people = {"Lucillia","Bert","Malina","Percy"};
            person = new JComboBox(people);
            String[] weapons = {"Broom","Scissors","Knife","Shovel","Shovel","IPad"};
            weapon = new JComboBox(weapons);
            String[] estates = {"Haunted House","Manic Manor","Visitation Villa","Calamity Castle","Peril Palace"};
            estate = new JComboBox(estates);

            JButton confirm = new JButton("Solve");
            confirm.addActionListener(e -> ConfirmPressed());

            this.setLayout(new GridBagLayout());

            JPanel selectBoxes = new JPanel();
            selectBoxes.add(person);
            selectBoxes.add(weapon);
            selectBoxes.add(estate);


            add(selectBoxes);
            add(confirm);

        }
        void ConfirmPressed(){
            Game g = Game.getGameInstance();

            boolean solve = g.getMurderer().checkMurderer(
                    g.getCardByName((String)weapon.getSelectedItem()),
                    g.getCardByName((String)estate.getSelectedItem()),
                    g.getCardByName((String)person.getSelectedItem())
            );

            guessing.setHasGuessed(true);
            Game.getGameInstance().solveMade(solve);



            if(solve)Game.getGameInstance().setGameState(Game.GameState.PlayerWon);//If solved correctly
            this.dispose();


            //if !solve and all guessed, lose
            boolean lose = solve;
            int i = 0;
            while(!lose){

                if( Game.getGameInstance().getPlayerMap().get(i) == null ){
                    Game.getGameInstance().setGameState(Game.GameState.PlayersLost);
                    break;
                }
                lose = !Game.getGameInstance().getPlayerMap().get(i).getHasGuessed(); //lose = false if player has guessed
                i++;
            }

            GameController.updateView();
        }
    }
}