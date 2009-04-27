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
} model;

model* readModel(string filename);

void writeModel(model* m);