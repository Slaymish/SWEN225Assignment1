import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Solve_GuessAttempts {

    public void TryGuess(String estate){
        GuessBox gb = new GuessBox(estate);

        gb.setLocation(400,300);
        gb.setSize(400,300);
        gb.setTitle("Guess");

        gb.pack();
        gb.setVisible(true);
    }

    public void TrySolve(){
        SolveBox sb = new SolveBox();

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
        GuessBox(String currentEstate){
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
            System.out.println();
            Game g = Game.getGameInstance();

            HashMap<Player,Card> cards = new HashMap<Player,Card>();

            var players = g.getPlayerMap();
            for(int i : players.keySet()){

                for(Card c : players.get(i).getCards()){
                    if(containsCard(c)){
                        cards.put(players.get(i),c);
                    }
                }
            }

            //display
            getContentPane().removeAll();
            repaint();
            for(Player p : cards.keySet()){
                add(new JLabel(p.getName() + " has " + cards.get(p)));
            }
            revalidate();

            boolean guess = false; // TODO check if guess is correct

            Game.getGameInstance().guessMade(guess);
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
        SolveBox(){
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
            System.out.println(solve);
            // TODO: change state if guess is correct/wrong
            Game.getGameInstance().solveMade(solve);
            this.dispose();
        }
    }
}
