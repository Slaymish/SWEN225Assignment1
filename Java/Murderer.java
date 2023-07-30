import java.util.*;
public class Murderer
{
  private Card weapon ;
  private Card estate ;
  private Card person ;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Murderer(Card _weapon, Card _estate, Card _person)
  {
    this.weapon = _weapon;
    this.estate = _estate;
    this.person = _person;
  }
  /**
   * Returns if the given cards are the murder ones
   */
  public boolean checkMurderer(Card _weapon, Card _estate, Card _person){
    return this.weapon.equals(_weapon)  && this.estate.equals(_estate) && this.person.equals(_person);
  }


  /**
   * Formatted display output
   */
  public String toString(Card weapon, Card estate, Card person){
    return "The Murderer was " + person.getCardName() + "in the" + estate.getCardName() + "with the " + weapon.getCardName();
  }
}
