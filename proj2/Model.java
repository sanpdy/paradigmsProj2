import java.util.ArrayList;

class Thing {
	public int x;
	public int y;
	public int kind;

	Thing(int x, int y, int kind) {
		this.x = x;
		this.y = y;
		this.kind = kind;

	}

	public int getX() {
		return x;
	}

	public int getY(int t) {
		return y;
	}

}

// Polymorphism: Extends the Thing class and adds a parameter of jumpDelay
class Jumper extends Thing {
	public int jumpDelay;

	public Jumper(int x, int y, int kind, int jumpDelay) {
		super(x, y, kind);
		this.jumpDelay = (int) (Math.random() * 100) % 20;
	}

	@Override
	public int getY(int t) {
		// Simulate jumping animation by adjusting the y-coordinate
		int adjustTime = t - jumpDelay;
		return super.getY(0) - (int) Math.max(0, 50 * Math.sin((double) (adjustTime) / 5));
	}
}

class Model {
	int turtle_x;
	int turtle_y;
	int dest_x;
	int dest_y;
	static int speed = 10;
	ArrayList<Thing> things = new ArrayList<Thing>();
	int selectedThing = 0;
	int currentTime = 0;

	Model() {
		this.turtle_x = 100;
		this.turtle_y = 100;
		this.dest_x = 150;
		this.dest_y = 100;
	}

	public void update() {
		if (this.turtle_x < this.dest_x)
			this.turtle_x += Math.min(speed, dest_x - turtle_x);
		else if (this.turtle_x > this.dest_x)
			this.turtle_x -= Math.max(speed, dest_x - turtle_x);
		if (this.turtle_y < this.dest_y)
			this.turtle_y += Math.min(speed, dest_y - turtle_y);
		else if (this.turtle_y > this.dest_y)
			this.turtle_y -= Math.max(speed, dest_y - turtle_y);
		currentTime++;
	}

	public void reset() {
		turtle_x = 100;
		turtle_y = 100;
		dest_x = turtle_x;
		dest_y = turtle_y;
	}

	public void setDestination(int x, int y) {
		this.dest_x = x;
		this.dest_y = y;
	}

	public static Thing createThing(int x, int y, int kind) {
		if (kind == 9) {
			return new Jumper(x, y, 1, 20); // Create a Jumper for turtles with jumpDelay
		} else {
			return new Thing(x, y, kind); // Create a regular Thing for other kinds
		}
	}

	public void addThing(View view, int mouseX, int mouseY) {
		// Adjust the mouse coordinates based on the scroll position
		int adjustedX = mouseX + view.getScrollX();
		int adjustedY = mouseY + view.getScrollY();

		// Add the object at the adjusted coordinates
		if (selectedThing == 9) {
			Jumper jumper = new Jumper(adjustedX, adjustedY, selectedThing, 20); // Adjust jumpDelay as needed
			things.add(jumper);
		} else {
			things.add(new Thing(adjustedX, adjustedY, selectedThing));
		}
	}

	public void removeThing(int x, int y) {
		double closestDistance = Double.MAX_VALUE;
		Thing closestThing = null;

		for (Thing thing : things) {
			double distance = Math.sqrt(Math.pow(thing.x - x, 2) + Math.pow(thing.y - y, 2));

			if (distance < closestDistance) {
				closestDistance = distance;
				closestThing = thing;
			}
		}

		if (closestThing != null) {
			things.remove(closestThing);
		}
	}

	// Load the things ArrayList into map.json
	public Json marshal() {
		Json map = Json.newObject();
		Json thingsList = Json.newList();

		for (Thing thing : this.things) {
			Json thingObj = Json.newObject();
			thingObj.add("x", thing.x);
			thingObj.add("y", thing.y);
			thingObj.add("kind", thing.kind);
			thingsList.add(thingObj);
		}
		map.add("things", thingsList);
		return map;
	}

	// Load the things.json back into the ArrayList
	public void unmarshal(Json things) {
		this.things.clear(); // Clear the existing list of things
		Json list_of_things = things.get("things");
		for (int i = 0; i < list_of_things.size(); i++) {
			Json thingObj = list_of_things.get(i);
			int x = (int) thingObj.getLong("x");
			int y = (int) thingObj.getLong("y");
			int kind = (int) thingObj.getLong("kind");
			if (kind == 9) {
				// Random number for jumpDelay
				int jumpDelay = (int) (Math.random() * 100) % 20;
				this.things.add(new Jumper(x, y, kind, jumpDelay));
			} else {
				this.things.add(new Thing(x, y, kind));
			}
		}
		// Update method
		update();
	}

}