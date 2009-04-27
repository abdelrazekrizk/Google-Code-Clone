#include "StdAfx.h"
#include "vertex.h"

// constructor
vertex::vertex(){
	x = 0; y = 0; z = 0;
}
vertex::vertex(float ix, float iy, float iz){
	x = ix; y = iy; z = iz;
}

// c = a + b vertex operation
vertex vertex::operator+(vertex b){
	vertex c;
	c.x = this->x + b.x;
	c.y = this->y + b.y;
	c.z = this->z + b.z;
	return c;
}

// c = a - b
vertex vertex::operator-(vertex b){
	vertex c;
	c.x = this->x - b.x;
	c.y = this->y - b.y;
	c.z = this->z - b.z;
	return c;
}

// scalar multiplication
vertex vertex::operator*(float f){
	vertex c;
	c.x = this->x * f;
	c.y = this->y * f;
	c.z = this->z * f;
	return c;
}

vertex vertex::operator/(float f){
	vertex c;
	c.x = x / f;
	c.y = y / f;
	c.z = z / f;
	return c;
}

// normalized cross product
vertex vertex::crossp(vertex v){
	vertex c;
	vertex a = v.normalize();
	vertex b = this->normalize();
	c.x = a.y * b.z - a.z * b.y;
	c.y = a.z * b.x - a.x * b.z;
	c.z = a.x * b.y - a.y * b.x;
	return c.normalize();
}

// returns a vertex in the direction of this vertex, but of length 1
vertex vertex::normalize(void){
	float lengthInv = 1.0f / length();
	return *this * lengthInv;
}

// length of vertex
float vertex::length(void){
	return sqrt(x * x + y * y + z * z);
}

float* vertex::toFloatArr(void){
	return (float*)this;
}

vertex vertex::clone(void){
	vertex v;
	v.x = this->x;
	v.y = this->y;
	v.z = this->z;
	return v;
}

// returns a vertex between this vertex and vertex b
vertex vertex::midpoint(vertex b){
	vertex v;
	v.x = (this->x + b.x) / 2;
	v.y = (this->y + b.y) / 2;
	v.z = (this->z + b.z) / 2;
	return v;
}