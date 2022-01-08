package o1.adventure

import scala.collection.mutable.Map


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var playerItems = Map[String, Item]()
  private var lifePoints = 2                        // Players life points, initial value is 2
  var westernGrass = false                          // These flags are for checking wether to add grass to new places
  var easternGrass = false
  var centralGrass = false
  private var turnCount = 0
  
  def getLifePoints = this.lifePoints               // Returns players lifepoints
   
  // Reduces one lifepoint from player
  
  def reducePoints = {
    this.lifePoints -= 1
  }
  
  // The player "eats" food
  
  def eat(food: String) = {
    if(this.currentLocation.containsFood(food)) {
      if (!food.contains("green")) {
        this.currentLocation.removeFood(food)
      }
      if(food.contains("green")) {
        this.lifePoints -= 1
        "Oh no! You ate fake grass that is used on the sport field! You lose one lifepoint!\n"
      }
      else {
        this.lifePoints += 1
        if (food == "eastern grass") {
          this.easternGrass = true
        }
        if (food == "western grass") {
          this.westernGrass = true
        }
        if (food == "grass") {
          this.centralGrass = true
        }
        "Mmmm, delicious grass! You gained one lifepoint!\n"
      }
    }
    else {
      "Unable to eat " + food
    }
  }
  
  
  // Open mystery box. Inform to use key if the player sees treasure chest
  
  def openChest = {
   if (this.currentLocation.contains("treasure chest")) { 
     if(this.playerItems.contains("key")) {
       "Use the key to open the treasure chest!\n"
     }
     else {
       "This treasure chest needs a key to open it! Maybe the humans could have posession of this key.\n"
     }
   }
   else if (this.currentLocation.contains("mystery box")) {
     this.currentLocation.removeItem("mystery box")
     this.playerItems("coffee") = new Item("coffee", "Hot drink that humans drink.\nApparently it can give superpowers when used!")
     "You found coffee in the mystery box!\n"
   }
   else {
     "Theres nothing to open here.\n"
   }
  }    
  
  def talk = {
    if (this.currentLocation.name == "North field") {
      if (this.playerItems.contains("key")) {
        "KVAAK KVAAK!"
      }
      else {
        this.playerItems("key") = new Item("key", "A shiny key. It could probably fit into somekind of chest...")
        "The people got scared of your noise and ran away.\nThey also seemed to drop something...\nIt looks like... A KEY!"
      }
    }
    else {
      "KVAAK KVAAK!"
    }
  }
  
  def use(itemName: String) = {
    if (this.currentLocation.name == "Forest" && (this.playerItems.contains("key")) && itemName == "key") {
      this.playerItems("energy drink") = new Item("energy drink", "Can of some odd drink that humans drink. Why would goose carry this?!")
      this.currentLocation.removeItem("treasure chest")
      "You opened the treasure chest...\nThere is...an energy drink!!!\nWhat do I do with this?!"
    }
    else if (itemName == "coffee" && this.playerItems.contains("coffee")) {
      this.lifePoints += 1
      this.playerItems.remove(itemName)
      "You gained one lifepoint by drinking coffee!\n"
    }
    else {
      "Unable to use " + itemName + " here.\n"
    }
  }
  
  // Check if the grass has been eaten, add new grass to random areas
  
  def hasEatenAllGrass = (this.westernGrass && this.easternGrass) || (this.centralGrass && this.westernGrass) || (this.centralGrass && this.easternGrass)
  
  def has(itemName: String) = this.playerItems.contains(itemName)
  
  def inventory = {
    if (!(this.playerItems.isEmpty)) {
      var output = "You are carrying:\n"
      for (k <- this.playerItems.keys) {
        output += k + "\n"
      }
      output
    }
    else {
      "You are empty-handed."
    }
  }
  
  def examine(itemName: String) = {
    if(this.playerItems.contains(itemName)) {
      "You look closely at the " + itemName + ".\n" + this.playerItems(itemName).description
    }
    else {
      "If you want to examine something, you need to get it first."
    }
  }
  
  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (this.currentLocation.name == "Start coast" && this.turnCount != 0) {
      this.turnCount += 1
      "You go " + direction + "foxbattle"
    }
    else {
      if (destination.isDefined) {
        this.turnCount += 1
        "You go " + direction + "." 
      }
      else {
        this.turnCount += 1
        "You can't go " + direction + "."
      }
    }
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }
  

  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


}


