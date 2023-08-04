/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

import java.util.*;

// line 180 "model.ump"
// line 259 "model.ump"
public class Door implements Cell {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Door Attributes
  private Estate estate;

  //Door Associations
  private List<Estate> estates;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Door(Estate aEstate)
  {
    super();
    estate = aEstate;
    estates = new ArrayList<Estate>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEstate(Estate aEstate)
  {
    boolean wasSet = false;
    estate = aEstate;
    wasSet = true;
    return wasSet;
  }

  public Estate getEstate()
  {
    return estate;
  }
  /* Code from template association_GetMany */
  public Estate getEstate(int index)
  {
    Estate aEstate = estates.get(index);
    return aEstate;
  }

  public List<Estate> getEstates()
  {
    List<Estate> newEstates = Collections.unmodifiableList(estates);
    return newEstates;
  }

  public int numberOfEstates()
  {
    int number = estates.size();
    return number;
  }

  public boolean hasEstates()
  {
    boolean has = estates.size() > 0;
    return has;
  }

  public int indexOfEstate(Estate aEstate)
  {
    int index = estates.indexOf(aEstate);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEstates()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addEstate(Estate aEstate)
  {
    boolean wasAdded = false;
    if (estates.contains(aEstate)) { return false; }
    estates.add(aEstate);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEstate(Estate aEstate)
  {
    boolean wasRemoved = false;
    if (estates.contains(aEstate))
    {
      estates.remove(aEstate);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEstateAt(Estate aEstate, int index)
  {  
    boolean wasAdded = false;
    if(addEstate(aEstate))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEstates()) { index = numberOfEstates() - 1; }
      estates.remove(aEstate);
      estates.add(index, aEstate);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEstateAt(Estate aEstate, int index)
  {
    boolean wasAdded = false;
    if(estates.contains(aEstate))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEstates()) { index = numberOfEstates() - 1; }
      estates.remove(aEstate);
      estates.add(index, aEstate);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEstateAt(aEstate, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    estates.clear();
    Cell.super.delete();
  }

  // line 188 "model.ump"
   public String toString(){
    return "Door";
    }


  // line 190 "model.ump"
   public boolean isEntrance(){
    return true;
  }

@Override
public String getDisplayChar() {
	// TODO Auto-generated method stub
	return "D";
}

}