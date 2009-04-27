#pragma once

#include <cmath>

// I know that I'm violating convention here, but it seems overkill to separate this into two files.

struct vertex {
	float x, y, z;

	// constructor
	vertex();
	vertex(float x, float y, float z);

	// c = a + b vertex operation
	vertex operator+(vertex b);

	// c = a - b
	vertex operator-(vertex b);

	// scalar multiplication
	vertex operator*(float f);

	vertex operator/(float f);

	// normalized cross product
	vertex crossp(vertex v);

	// returns a vertex in the direction of this vertex, but of length 1
	vertex normalize(void);

	// length of vertex
	float length(void);

	// a float array representing this vertex
	float* toFloatArr(void);

	vertex clone(void);

	vertex midpoint(vertex v2);
};