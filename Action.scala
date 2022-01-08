package o1.adventure


/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as "go east" or "rest" */
class Action(input: String) {

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim


  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as "You go west."). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player) = this.verb match {
    case "go"    => Some(actor.go(this.modifiers))
    case "quit"  => Some(actor.quit())
    case "inventory" => Some(actor.inventory)
    case "examine" => Some(actor.examine(this.modifiers))
    case "eat" => Some(actor.eat(this.modifiers))
    case "help" => Some("Find places that contain grass and eat it before the winter arrives to gain lifepoints.\nOnce you have gained at least 4 lifepoints go back to start point (coast).\nAlso look around the forest, you might find something interesting.\nCommands: go 'point', quit, inventory, eat 'food name', use 'item name', open, talk, examine 'item name', help")
    case "talk" => Some(actor.talk)
    case "open" => Some(actor.openChest)
    case "use" => Some(actor.use(this.modifiers))
    case other   => None
  }


  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"


}

