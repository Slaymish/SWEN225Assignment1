/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import java.util.List;
import java.util.Set;

// line 113 "model.ump"
// line 239 "model.ump"
public class Estate
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Estate Attributes
  public String name;
  private List<Inside> estateCells;
  private Set<Person> playersInEstate;
  //private Weapon weapon;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Estate(List<Inside> aEstateCells, Set<Person> aPlayersInEstate)
  {
    estateCells = aEstateCells;
    playersInEstate = aPlayersInEstate;
    //weapon = aWeapon;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEstateCells(List<Inside> aEstateCells)
  {
    boolean wasSet = false;
    estateCells = aEstateCells;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayersInEstate(Set<Person> aPlayersInEstate)
  {
    boolean wasSet = false;
    playersInEstate = aPlayersInEstate;
    wasSet = true;
    return wasSet;
  }

  /*
  public boolean setWeapon(Weapon aWeapon)
  {
    boolean wasSet = false;
    weapon = aWeapon;
    wasSet = true;
    return wasSet;
  }

   */

  public List<Inside> getEstateCells()
  {
    return estateCells;
  }

  public Set<Person> getPlayersInEstate()
  {
    return playersInEstate;
  }

  /*
  public Weapon getWeapon()
  {
    return weapon;
  }

   */

  public void delete()
  {}


  /**
   * Adds a person to the players in the estate
   */
  // line 123 "model.ump"
  public void addPerson(Person person){
    
  }


  /**
   * Removes a person to the players in the estate
   */
  // line 130 "model.ump"
  public boolean removePerson(Person person){
    return false;
  }


  /**
   * Sets Weapon
   */
  // line 137 "model.ump"
  public void setWeapon(Person person){
    
  }

  // line 141 "model.ump"
  public String toString(){
    return "Estate" + this.name;
  }
}