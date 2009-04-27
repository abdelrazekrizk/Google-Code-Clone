#pragma once

#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include "vertex.h"

using namespace std;

typedef struct {
	vector<vertex*> vertices;
	vector<vector<int>> faces;
	// faces adjacent to a vertex of id index.
	vector<vector<int>> adjacentFaces;
} model;

model* readModel(string filename);

void writeModel(model* m);