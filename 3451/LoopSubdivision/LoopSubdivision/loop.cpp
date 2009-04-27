#include "StdAfx.h"

#include "loop.h"

#pragma warning( disable : 4244 ) // double -> float possible loss of data warning
#pragma warning( disable : 4018 ) // signed/unsigned comparison warning
#pragma warning( disable : 4267 ) // size_t -> int possible loss of data warning

// Fancy!
const float PI = 4.0 * atan(1.0);

void modelcpy(model *a, model *b){
	b->faces.clear();
	b->vertices.clear();
	for(int i = 0; i < a->faces.size(); i++){
		b->faces.push_back(*(new vector<int>()));
		for each(int id in a->faces[i]){
			b->faces[i].push_back(id);
		}
	}
	//for(int i = 0; i < a->vertices.size(); i++){
	for each (vertex *v in a->vertices){
		b->vertices.push_back(new vertex(*v));
	}

	for (int i = 0; i < a->adjacentFaces.size(); i++){
		b->adjacentFaces.push_back(*(new vector<int>()));
		for each(int id in a->adjacentFaces[i]){
			b->adjacentFaces[i].push_back(id);
		}
	}
}

int getMidpointId(vector<vector<int>> *midpointTable, int v1id, int v2id){
	int result = (*midpointTable)[v1id][v2id];
	if(result != -1) return result;
	else return (*midpointTable)[v2id][v1id];
}

model* subdivide(model* m){
	fprintf(stdout, "Dividing each polygon in model into four polygons.\n%i polygons to divide...\n", m->faces.size());
	model *divided = new model;

	vector<vector<int>> midptTable(m->vertices.size(), vector<int>(m->vertices.size(), -1));

	for each(vertex* v in m->vertices){
		divided->vertices.push_back(new vertex(*v));
	}

	for each(vector<int> face in m->faces){
		vector<int> newVertices(3);
		for(int j = 0; j < 3; j++){
			int v1id = face[j];
			int v2id = face[(j+1) % 3];

			int mid;
			if((mid = getMidpointId(&midptTable, v1id, v2id)) != -1){
				newVertices[j] = mid;
			} else {
				vertex* v1 = divided->vertices[face[j]];
				vertex* v2 = divided->vertices[face[(j+1) % 3]];
				divided->vertices.push_back(new vertex(v1->midpoint(*v2)));
				int newVertexId = (int)divided->vertices.size() - 1;
				newVertices[j] = newVertexId;
				midptTable[v1id][v2id] = newVertexId;
			}
		}

		for(int j = 0; j < 3; j++){
			vector<int> newFace(3);
			newFace[0] = face[j];
			newFace[1] = newVertices[j];
			newFace[2] = newVertices[(j+2)%3];
			divided->faces.push_back(*(new vector<int>(newFace)));
		}

		vector<int> middleFace;
		for each (int d in newVertices){
			middleFace.push_back(d);
		}
		divided->faces.push_back(middleFace);
	}
	// construct adjacent faces table
	divided->adjacentFaces = *(new vector<vector<int>>(divided->vertices.size()));
	for (int i = 0; i < divided->faces.size(); i++){
		vector<int> face = divided->faces[i];
		for each (int id in face){
			divided->adjacentFaces[id].push_back(i);
		}
	}
	return divided;
}

bool vectorContains(vector<int>* connected, int vertexId){
	for each (int id in *connected){
		if(id == vertexId) return true;
	}
	return false;
}

vector<int> connectedVertices(model* m, int vertexId){
	vector<int> connected;
	for each(int i in m->adjacentFaces[vertexId]){
		vector<int> face = m->faces[i];
		for each(int id in face){
			if(id != vertexId && !vectorContains(&connected, id)){
				connected.push_back(id);
			}
		}
	}
	return connected;
}

float beta(int n){
	return 5.0 / 4.0 - pow(3.0 + 2.0 * cos(2 * PI / n), 2) / 32.0;
}

float alpha(int n){
	float b = beta(n);
	return n * (1.0 - b) / b;
}

model* average(model* m){
	fprintf(stdout, "Averaging vertexes.\n%i vertexes to average...\n", m->vertices.size());
	model* averaged = new model;
	modelcpy(m, averaged);
	for(int i = 0; i < averaged->vertices.size(); i++){ // for each vertex get connected vertices
		vector<int> connected = connectedVertices(m, i);
		int n = connected.size();
		float a = alpha(n);
		vertex v = *m->vertices[i] * (a / (a + n));
		for each (int j in connected){
			v = v + (*m->vertices[j] * (1.0 / (a + n)));
		}
		averaged->vertices[i] = new vertex(v);
		if((averaged->vertices.size() - i) % 500 == 0){
			fprintf(stdout, "%i vertices left...\n", averaged->vertices.size() - i);
		}
	}
	return averaged;
}

model* loop(model* m, int times){
	if(times == 0){
		fprintf(stdout, "Subdivision finished!\n");
		return m;
	} else { // todo: better memory management
		fprintf(stdout, "Subdividing model. %d more subdivision(s) to go...\n", times);
		model* subdivided = subdivide(m);
		model* averaged = average(subdivided);
		//delete subdivided;
		return loop(averaged, times - 1);
	}
}
