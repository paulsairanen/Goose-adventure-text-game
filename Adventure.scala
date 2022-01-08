package o1.adventure
import scala.util.Random

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "Goose adventure"
  
  //Goose adventure areas, the map is a 3x3 grid
  
  private val northNorth    = new Area("North field", "You are at the northern field.\nThere are humans everywhere!\n")
  private val northWest     = new Area("Grass field", "You are at the grass field.\n")
  private val northEast     = new Area("Amfitheater", "You have arrived at a beautiful amfitheater!\n")
  private val east          = new Area("Forest",      "You are somewhere in the forest. There are a lot of trees here.\n")
  private val west          = new Area("Sport field", "you have arrived on the human sport field.\nThere's lots of delicious looking green stuff!\n")
  private val center        = new Area("Open field",  "You are at an open field. All the grass has been eaten by other geese.\n")
  private val southEast     = new Area("South Forest","You are somewhere in the forest. There are a lot of trees here.\n")
  private val southWest     = new Area("Coast",       "You are at the coast. The ocean waves are hitting the rocks.\n")
  private val startCoast    = new Area("Start coast", "You are at the coast. The ocean waves are hitting the rocks.\nYour fellow goose friends are enjoying the summer.\n")
  private val startingPoint = startCoast
  
  
  // Goose adventure neighbors
         
  center.setNeighbors(Vector("north" -> northNorth, "east" -> east, "south" -> startCoast, "west" -> west)) 
  northNorth.setNeighbors(Vector("east" -> northEast, "south" -> center, "west" -> northWest))
  northEast.setNeighbors(Vector("south" -> east, "west" -> northNorth))
  northWest.setNeighbors(Vector("east" -> northNorth, "south" -> west))
  east.setNeighbors(Vector("north" -> northEast,   "south" -> southEast, "west" -> center))
  west.setNeighbors(Vector("north" -> northWest, "south" -> southWest, "east" -> center))
  startCoast.setNeighbors(Vector("north" -> center, "east" -> southEast,   "west" -> southWest))  
  southEast.setNeighbors(Vector("north" -> east, "west" -> startCoast))
  southWest.setNeighbors(Vector("north" -> west, "east" -> startCoast))

  // Goose adventure foods and items. You can also get other items that are not listed here.
  
  northWest.addFood(new Food("western grass", "It looks fresh and delicious"))
  northEast.addFood(new Food("eastern grass", "It looks fresh and delicious"))
  east.addItem(new Item("treasure chest", "Mysterious treasure chest. What might be inside?"))
  west.addFood(new Food("green stuff", "Yuck! It's fake grass used in the sport field! Damn humans!"))
  southWest.addItem(new Item("mystery box", "Mysterious box. What might be inside?"))
  
  
  /** The character that the player controls in the game. */
  val player = new Player(startCoast)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.startingPoint && player.getLifePoints > 3 && this.player.has("energy drink")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || player.getLifePoints == 0

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You have arrived to the south coast of Finland to spend the summer with all your goose friends.\nGet at least four (4) lifepoints in total by eating grass so that you have energy to fly away before the winter arrives. You have now two (2) lifepoints.\nAlso look around, you might find something interesting.\n"


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  
  // Goose adventure goodbyemessage

  def goodbyeMessage = {
    if (this.isComplete)
      "The goose flew happily south to spend the winter with all the friends.\nCongratulations you won the game."
    else if (this.turnCount == this.timeLimit)
      "Winter arrived and it is too cold.\n"
    else  // game over due to player quitting
      "This gooses adventure has ended :'(\nThe game is over."
  }
    

  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
    }
    if (this.player.hasEatenAllGrass) {
      this.addFoods
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }
  
  // After the first two grasses are eaten, this adds two new ares with grass randomly 
  
  def addFoods = {
    if (Random.nextBoolean) {
      this.northWest.addFood(new Food("western grass", "It looks fresh and delicious"))
      this.center.addFood(new Food("grass", "It looks fresh and delicious"))
      this.player.centralGrass = false
      this.player.westernGrass = false
      this.player.easternGrass = false
    }
    else {
      this.center.addFood(new Food("grass", "It looks fresh and delicious"))
      this.northEast.addFood(new Food("eastern grass", "It looks fresh and delicious"))
      this.player.centralGrass = false
      this.player.westernGrass = false
      this.player.easternGrass = false
    }
  }


}

