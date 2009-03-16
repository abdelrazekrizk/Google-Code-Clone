import processing.core.*;
import java.util.*;

public class parser extends PApplet {
	
	// "constants"
	private final int width = 600;
	private Color WHITE = new Color(1.0f, 1.0f, 1.0f);
	private Color BLACK = new Color(0.0f, 0.0f, 0.0f); 
	private final float EPSILON = 0.0001f; // small value for floating point math.
	private final float INF = Float.POSITIVE_INFINITY; // large value!
	private Vector ORIGIN = new Vector(0, 0, 0);
	
	// runtime variables
	private String gCurrentFile = new String("rect_test.cli");
	private Color backgroundColor = null;
	private float fovAngle = 0;
	private ArrayList<RayTracable> sceneShapes = null;
	private ArrayList<Light> lights = null;
	private Material currentSurface = null;
	private boolean initialized = false;
	
	/**
	 * Testing function for rays
	 */
	public void mousePressed(){
		Vector source = ORIGIN;
		float z = -1 * (width / 2.0f) / tan(fovAngle/2);
		Vector direction = new Vector(-width / 2.0f + mouseX, width / 2.0f - mouseY, z).normalize();
		ArrayList<TraceResult> traceResults = new ArrayList<TraceResult>();
		for(RayTracable shape : sceneShapes){
			ArrayList<Vector> vectors = shape.IntersectTest(source, direction);
			if(vectors != null){
				for(Vector v : vectors){
					if(v != null && shape.NormalAt(v) != null && shape != null){
						TraceResult r = new TraceResult(v, shape.NormalAt(v), shape, WHITE);
						traceResults.add(r);
					}
				}
			}
		}
		println(String.format("Clicked pixel: (%d, %d) corresponding to direction %s", mouseX, mouseY, direction));
		if(!traceResults.isEmpty()){
			println(String.format("%d intersections:", traceResults.size()));
			for(TraceResult r : traceResults){
				Vector normal = r.intersectedShape.NormalAt(r.intersection);
				println(String.format("sceneObject[%d] (dist: %.2f) %s at %s [Normal: %s]", sceneShapes.indexOf(r.intersectedShape), r.intersection.length(), r.intersectedShape, r.intersection, normal));
			}
			TraceResult r = Trace(source, direction);
			println(String.format("Result of the Trace: sceneObject[%d] (dist: %.2f) %s at %s", sceneShapes.indexOf(r.intersectedShape), r.intersection.length(), r.intersectedShape, r.intersection));
		} else {
			println("Hit nothing.");
		}
	}
	
	private void rayTrace() {
		if(initialized){
			loadPixels();
			// let a ray be <px, py, z> where px, py are cartisan coords of screen pixels, then z must be:
			float z = -1 * (width / 2.0f) / tan(fovAngle/2);
			for(int x = 0; x < width; x++){
				for(int y = 0; y < width; y++){
					Vector source = ORIGIN;
					Vector direction = new Vector(-width / 2.0f + x, width / 2.0f - y, z).normalize();
					TraceResult rayTrace = TraceWithColor(source, direction, 10);
					
					if(rayTrace != null){
						pixels[x + y * width] = rayTrace.color.toProcessingColor();
					} else {
						pixels[x + y * width] = backgroundColor.toProcessingColor();
					}
				}
			}
			updatePixels();
		} else {
			error("Scene was not intialized properly.");
		}
	}
	
	public TraceResult TraceWithColor(Vector source, Vector direction, int reflectiveDepth){
		TraceResult result = Trace(source, direction);
		if(result != null){
			RayTracable nearestShape = result.intersectedShape;
			Vector nearestLoc = result.intersection;
			Material material = nearestShape.material;
			Color ambi = material.ambient;
			Color diff = material.diffuse;
			Color spec = material.specular;
			float p    = material.phong;
			
			Vector n = nearestShape.NormalAt(nearestLoc);
			Vector eye = direction.fTimes(-1);
	
			Color pixelColor = ambi;
			
			for(Light light : lights){
				Color lightColor = light.color;
				Vector lightVector = light.location.vMinus(nearestLoc).normalize();
				Vector halfway = eye.vPlus(lightVector).normalize();
				TraceResult lightTrace = Trace(nearestLoc, lightVector);
				if(lightTrace == null || lightTrace.intersectedShape == nearestShape){
					Color diffuse = lightColor.times(diff.times(max(0, n.dot(lightVector))));
					Color specular = lightColor.times(spec.times(max(0, pow(halfway.dot(n), p*2))));
					pixelColor = pixelColor.plus(diffuse).plus(specular);
				}
			}
			
			if(material.reflectK != 0.0f && reflectiveDepth > 0){
				Color reflected;
				Vector r = direction.vMinus(n.fTimes(2 * (direction.dot(n)))).normalize();
				TraceResult reflectTrace = TraceWithColor(nearestLoc, r, reflectiveDepth - 1);
//				reflectTrace = null;
				if(reflectTrace != null){
					reflected = reflectTrace.color;
				} else {
					reflected = backgroundColor.clone();
				}
//				pixelColor = pixelColor.times(1 - material.reflectK).plus(reflected.times(material.reflectK));
				pixelColor = pixelColor.plus(reflected.times(material.reflectK));
			}
			
			result.color = pixelColor;
		}
		return result;
	}
	
	public TraceResult Trace(Vector source, Vector direction){
		Vector nearestLoc = null;
		RayTracable nearestShape = null;
		for(RayTracable shape : sceneShapes){
			ArrayList<Vector> intersections = shape.IntersectTest(source, direction);
			if(intersections != null){
				for(Vector v : intersections){
					if((nearestLoc == null || nearestLoc.vMinus(source).length() > v.vMinus(source).length()) && abs(v.vMinus(source).length()) > EPSILON){
						nearestLoc = v;
						nearestShape = shape;
					}
				}
			}
		}
		if(nearestLoc != null){
			return new TraceResult(nearestLoc, nearestShape.NormalAt(nearestLoc), nearestShape, WHITE.clone());
		} else {
			return null;
		}
	}
	
	public class TraceResult {
		public Vector intersection;
		public Vector normal;
		public RayTracable intersectedShape;
		public Color color;
		public TraceResult(Vector intersection, Vector normal, RayTracable intersected, Color color){
			this.intersection = intersection;
			this.normal = normal;
			this.intersectedShape = intersected;
			this.color = color;
		}
	}

	private void createSphere(ArrayReader r) {
		Vector center = r.readVector();
		float radius = r.readFloat();
		Sphere s = new Sphere(center, radius);
		sceneShapes.add(s);
	}
	private void createCylinder(ArrayReader r) {
		Vector center = r.readVector();
		float height = r.readFloat();
		float radius = r.readFloat();
		Cylinder c = new Cylinder(center, height, radius);
		sceneShapes.add(c);
	}
	private void changeMaterial(ArrayReader r) {
		Color ambient  = r.readColor(),
		      diffuse  = r.readColor(),
		      specular = r.readColor();
		currentSurface = new Material(ambient, diffuse, specular, r.readFloat(), r.readFloat());
	}
	private void createLight(ArrayReader r) {
		Vector location = r.readVector();
		Color color = r.hasRemaining() ? r.readColor() : WHITE.clone();
		Light newLight = new Light(location, color);
		lights.add(newLight);
	}
	private void changeBackground(ArrayReader r) {
		backgroundColor = r.readColor();
	}
	private void changeFov(ArrayReader r) {
		fovAngle = radians(r.readFloat());
	}

	private void initializeNewScene() {
		backgroundColor = BLACK.clone();
		fovAngle = radians(90.0f);
		currentSurface = new Material(WHITE, WHITE, WHITE, 1, 1);
		lights = new ArrayList<Light>();
		sceneShapes = new ArrayList<RayTracable>();
		initialized = true;
	}
	
	// abstract class for an object that can be ray-traced.
	public abstract class RayTracable {
		public Material material;
		public RayTracable(){
			material = currentSurface.clone();
		}
		public abstract ArrayList<Vector> IntersectTest(Vector source, Vector direction);
		public abstract Vector NormalAt(Vector v);
		public Material Material(){
			return material;
		}
	}
	
	public class Sphere extends RayTracable {
		public Vector center;
		public float radius;
		
		public Sphere(Vector center, float radius){
			this.center = center;
			this.radius = radius;
		}
		
		public ArrayList<Vector> IntersectTest(Vector source, Vector direction) {
			ArrayList<Vector> hits = null;
			Vector v = source.vMinus(center);
			float vDotDir = v.dot(direction);
			float discriminant = vDotDir * vDotDir - v.squared() + radius * radius;
			if(discriminant >= 0){
				hits = new ArrayList<Vector>();
				float tp = -vDotDir + sqrt(discriminant);
				float tm = -vDotDir - sqrt(discriminant);
				if(tp > EPSILON) // we're using a ray - we don't want solutions that give negative direction vectors.
					hits.add(source.vPlus(direction.fTimes(tp)));
				if(tm > EPSILON)
					hits.add(source.vPlus(direction.fTimes(tm)));
			}
			return hits;
		}
		
		public Vector NormalAt(Vector v) {
			return v.vMinus(center).normalize();
		}
		
		public String toString(){
			return String.format("Sphere with center: %s, radius: %.2f", center, radius);
		}
	}
	
	public class Cylinder extends RayTracable {
		Vector center;
		float height, radius;
		
		public Cylinder(Vector center, float height, float radius){
			this.center = center; this.height = height; this.radius = radius;
		}
		
		public ArrayList<Vector> IntersectTest(Vector source, Vector direction) {
			ArrayList<Vector> hits = new ArrayList<Vector>();	
			float t, x, y, z, a, b, c, det, tplus, tminus;
			Vector displacement, result;
			
			// Walls
			// FIXME
//			displacement = source.vMinus(center);
//			a = direction.x * direction.x + direction.z * direction.z;
//			b = 2 * (direction.x * (displacement.x) + direction.z * (displacement.z));
//			c = displacement.x * displacement.x + displacement.z * displacement.z - radius * radius;
//			det = b * b - 4 * a * c;
//			if(det >= EPSILON){
//				tplus = -b + sqrt(det) / (2 * a);
//				result = source.vPlus(direction.fTimes(tplus));
//				if(result.y > center.y && result.y < center.y + height)
//					hits.add(result);
//				tminus = -b - sqrt(det) / (2 * a);
//				result = source.vPlus(direction.fTimes(tminus));
//				if(result.y > center.y && result.y < center.y + height)
//					hits.add(result);
//			}
			
			
			// Top - FIXME?
			t = (center.y + height - source.y) / direction.y;
			if(t > -EPSILON){
				x = source.x + direction.x * t;
				y = center.y + height - source.y;
				z = source.z + direction.z * t;
				Vector soln = new Vector(x, y, z);
//				Vector soln = source.vPlus(direction.fTimes(t));
//				soln.y = center.y + height - source.y;
				a = pow(soln.x - center.x, 2) + pow(soln.z, 2) - radius * radius;
				if(a <= EPSILON){
					hits.add(soln);
				}
			}
			
			// Bottom - FIXME
//			t = (center.y - source.y) / direction.y;
//			if(t > -EPSILON){
//				Vector soln = source.vPlus(direction.fTimes(t));
//				soln.y = center.y - source.y;
//				a = pow(soln.x - center.x, 2) + pow(soln.z, 2) - radius * radius;
//				if(a <= EPSILON){
//					hits.add(soln);
//				}
//			}
			
			return hits;
		}

		public Vector NormalAt(Vector v) {
			if(abs(v.y - center.y) < EPSILON){
				return new Vector(0, -1, 0);
			} else if(abs(v.y - center.y - height) < EPSILON){
				return new Vector(0, 1, 0);
			} else {
				return new Vector(v.x - center.x, 0, v.z - center.z).normalize();
			}
//			return new Vector(0, 1, 0);
		}
		
		public String toString(){
			return String.format("Cylinder with bottom-center: %s, height: %.2f, radius: %.2f", center, height, radius);
		}
	}
	
	public class Light {
		Vector location;
		Color color;
		public Light(Vector loc, Color c){
			this.location = loc; this.color = c;
		}
		public Light(Vector loc){
			this.location = loc; this.color = WHITE.clone();
		}
	}
	
	public class Material {
		public float phong, reflectK;
		public Color ambient, diffuse, specular;
		public Material(Color ambient, Color diffuse, Color specular, float phong, float reflect){
			this.phong = phong; this.reflectK = reflect;
			this.ambient = ambient; this.diffuse = diffuse; this.specular = specular;
		}
		public Material clone(){
			return new Material(ambient.clone(), diffuse.clone(), specular.clone(), phong, reflectK);
		}
	}
	
	// Helper classes, created by myself (Java probably has its own versions of these classes,
	// I decided to make my own for certainty however.).
	public class Vector {
		public float x, y, z;
		public Vector(float x, float y, float z){
			this.x = x; this.y = y; this.z = z;
		}
		public float dot(Vector v){
			return x * v.x + y * v.y + z * v.z;
		}
		public Vector fTimes(float f){
			return new Vector(x * f, y * f, z * f);
		}
		public Vector vMinus(Vector b){
			return new Vector(x - b.x, y - b.y, z - b.z);
		}
		public Vector fMinus(float f){
			return new Vector(x - f, y - f, z - f);
		}
		public Vector vPlus(Vector b){
			return new Vector(x + b.x, y + b.y, z + b.z);
		}
		public Vector normalize(){
			return this.fDivide(this.length());
		}
		public Vector fDivide(float f){
			return fTimes(1 / f);
		}
		public float length(){
			return sqrt(squared());
		}
		public float squared(){
			return dot(this);
		}
		public String toString(){
			return String.format("(%.2f, %.2f, %.2f)", x, y, z);
		}
	}
	public class Color {
		public float r, g, b;
		public Color(float red, float green, float blue) {
			this.r = red; this.g = green; this.b = blue;
		}
		public int toProcessingColor(){
			return color(r, g, b);
		}
		public Color plus(Color c){
			return new Color(min(1, c.r + r), min(1, c.g + g), min(1, c.b + b));
		}
		public Color times(Color c){
			return new Color(min(1, c.r * r), min(1, c.g * g), min(1, c.b * b));
		}
		public Color times(float f){
			return new Color(min(1, r * f), min(1, g * f), min(1, b * f));
		}
		public Color clone(){
			return new Color(r, g, b);
		}
	}
	
	/**
	 * Sequential reader for a string array.
	 */
	public class ArrayReader{
		private String[] array;
		private int position;
		public ArrayReader(String[] array){
			this.array = array;
			position = 1;
		}
		public String readString(){
			return array[position++];
		}
		public float readFloat(){
			return get_float(array[position++]);
		}
		public Vector readVector(){
			float x = readFloat();
			float y = readFloat();
			float z = readFloat();
			return new Vector(x, y, z);
		}
		public Color readColor(){
			float r = readFloat();
			float g = readFloat();
			float b = readFloat();
			return new Color(r, g, b);
		}
		public float remaining(){
			return array.length - position;
		}
		public boolean hasRemaining(){
			return remaining() > 0;
		}
	}
	public void keyPressed() {
		switch(key) {
			case '1':  gCurrentFile = new String("t0.cli"); interpreter(); break;
			case '2':  gCurrentFile = new String("t1.cli"); interpreter(); break;
			case '3':  gCurrentFile = new String("t2.cli"); interpreter(); break;
			case '4':  gCurrentFile = new String("t3.cli"); interpreter(); break;
			case '5':  gCurrentFile = new String("c0.cli"); interpreter(); break;
			case '6':  gCurrentFile = new String("c1.cli"); interpreter(); break;
			case '7':  gCurrentFile = new String("c2.cli"); interpreter(); break;
			case '8':  gCurrentFile = new String("c3.cli"); interpreter(); break;
			case '9':  gCurrentFile = new String("c4.cli"); interpreter(); break;
			case '0':  gCurrentFile = new String("c5.cli"); interpreter(); break;
		}
	}
	
	void interpreter() {
		initializeNewScene();
		String str[] = loadStrings(gCurrentFile);
		if (str == null) println("Error! Failed to read the file.");
		for (int i=0; i<str.length; i++) {
			String[] token = splitTokens(str[i], " "); // Get a line and parse tokens.
			if (token.length == 0) 
				continue; // Skip blank line.
			ArrayReader reader = new ArrayReader(token);
			if (token[0].equals("fov")) {
				changeFov(reader);
			} else if (token[0].equals("background")) {
				changeBackground(reader);
			} else if (token[0].equals("light")) {
				createLight(reader);
			} else if (token[0].equals("surface")) {
				changeMaterial(reader);
			} else if (token[0].equals("cyl")) {
				createCylinder(reader);
			} else if (token[0].equals("sphere")) {
				createSphere(reader);
			} else if (token[0].equals("color")) {
				changeColor(reader);
			} else if (token[0].equals("rect")) {
				createRectangle(reader);
			} else if (token[0].equals("write")) {
				rayTrace();
				save(token[1]);  
			}
		}
	}
	
	/**
	 * Frame drawing loop
	 */
	public void draw() {
		// do nothing, no animation or anything
	}
	
	/**
	 * Some initializations for the scene.
	 */
	public void setup() {
		size(width, width);  
		noStroke();
		colorMode(RGB, 1.0f);
		background(0, 0, 0);
		interpreter();
	}
	
	private void createRectangle(ArrayReader r){
		float x0 = r.readFloat(), 
		      y0 = r.readFloat(),
		      x1 = r.readFloat(),
		      y1 = r.readFloat();
		rect(x0, height-y1, x1-x0, y1-y0);
	}

	private void changeColor(ArrayReader r) {
		float red = r.readFloat(),
		      green = r.readFloat(),
		      blue = r.readFloat();
		fill(red, green, blue);
	}
	
	float get_float(String str) { 
		return Float.parseFloat(str); 
	}
	
	private void error(String errMsg){
		println(String.format("ERROR: %s", errMsg));
		exit();
	}
}
