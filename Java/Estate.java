/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/



// line 113 "model.ump"
// line 239 "model.ump"
public class Estate
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Estate Attributes
  private List<Cell> estateCells;
  private Set<Person> playersInEstate;
  private Weapon weapon;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Estate(List<Cell> aEstateCells, Set<Person> aPlayersInEstate, Weapon aWeapon)
  {
    estateCells = aEstateCells;
    playersInEstate = aPlayersInEstate;
    weapon = aWeapon;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEstateCells(List<Cell> aEstateCells)
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

  public boolean setWeapon(Weapon aWeapon)
  {
    boolean wasSet = false;
    weapon = aWeapon;
    wasSet = true;
    return wasSet;
  }

  public List<Cell> getEstateCells()
  {
    return estateCells;
  }

  public Set<Person> getPlayersInEstate()
  {
    return playersInEstate;
  }

  public Weapon getWeapon()
  {
    return weapon;
  }

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
    
  }


  /**
   * Sets Weapon
   */
  // line 137 "model.ump"
  public void setWeapon(Person person){
    
  }

  // line 141 "model.ump"
  public String toString(){
    
  }

}