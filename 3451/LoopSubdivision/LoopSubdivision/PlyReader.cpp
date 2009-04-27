#include "StdAfx.h"
#include "PlyReader.h"
#pragma warning( disable : 4996 ) // unsafe function warning (strtok, strcpy, etc)
#pragma warning( disable : 4244 ) // double->float loss of data warning

///////////////////////////////////
// private prototypes
int readInt(vector<string>* file, int line, int place);
vertex* readVertex(vector<string> *file, int lineNum);
vector<int>* readIntArray(vector<string> *file, int lineNum);

model* readModel(string filename){
	vector<string> entireFile;

	ifstream fin(filename.c_str());
	if(fin.is_open()){
		string line;
		while(getline(fin, line)){
			entireFile.push_back(line);
		}
		fin.close();
	} else {
		fprintf(stdout, "File could not be opened.");
		exit(0);
	}

	int place = 0;

	model* m = new model;

	int vertexCount = readInt(&entireFile, place++, 7);
	int faceCount = readInt(&entireFile, place++, 5);
	for(int i = 0; i < vertexCount; i++){
		m->vertices.push_back(readVertex(&entireFile, place++));
	}
	for(int i = 0; i < faceCount; i++){
		m->faces.push_back(*readIntArray(&entireFile, place++));
	}

	return m;
}

int readInt(vector<string> *file, int lineNum, int place){
	string line = file->at(lineNum);
	return atoi(line.substr(place).c_str());
}

vertex* readVertex(vector<string> *file, int lineNum){
	vertex* v = new vertex;
	string line = file->at(lineNum);
	char* str = new char[line.length()];
	strcpy(str, line.c_str());
	char* piece;
	piece = strtok(str, " \0");
	v->x = atof(piece);
	piece = strtok(NULL, " \0");
	v->y = atof(piece);
	piece = strtok(NULL, " \0");
	v->z = atof(piece);
	return v;
}

vector<int>* readIntArray(vector<string> *file, int lineNum){
	vector<int> *iv = new vector<int>();
	string line = file->at(lineNum);
	char* str = new char[line.length()];
	strcpy(str, line.c_str());
	char* piece;
	piece = strtok(str, " \0");
	piece = strtok(NULL, " \0");
	iv->push_back(atoi(piece));
	piece = strtok(NULL, " \0");
	iv->push_back(atoi(piece));
	piece = strtok(NULL, " \0");
	iv->push_back(atoi(piece));
	return iv;
}

void writeModel(model* m){
	ofstream fout("temp.ply");
	fout << "vertex ";
	fout << m->vertices.size();
	fout << "\nface ";
	fout << m->faces.size();
	for each (vertex* v in m->vertices){
		fout << "\n" << v->x << " " << v->y << " " << v->z;
	}
	for each (vector<int> face in m->faces){
		fout << "\n" << face.size();
		for(int i = 0; i < face.size(); i++){
			fout << " " << face[i];
		}
	}
	fout.close();
}