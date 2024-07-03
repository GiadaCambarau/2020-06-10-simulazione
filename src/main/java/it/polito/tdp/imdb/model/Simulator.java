package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	//input
	private int giorno;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private List<Actor> attori;
	private List<Actor> rimasti;
	private List<Actor> intervistati;
	
	private int pause;
	
	public Simulator(Graph<Actor, DefaultWeightedEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void initialize(int n) {
		this.giorno =n;
		this.attori = new ArrayList<>(this.grafo.vertexSet());
		this.intervistati = new ArrayList<>();
		this.pause =0;
		
	}
	public void run() {
		for (int i =1; i<giorno; i++) {
			//giorno uno
			if (i==1) {
				double random = Math.random();
				int indice = (int) (random*attori.size());
				Actor a = attori.get(indice);
				attori.remove(a);
				intervistati.add(a);
				break;
			}
			//dal secondo giorno in poi
			 if (i>=3) {
				
				Actor precedente = intervistati.get(intervistati.size()-1);
				Actor precedente2 = intervistati.get(intervistati.size()-2);
				if (precedente != null && precedente2 != null) {
					if (precedente.gender.equals(precedente2.gender)) {
						double random3 = Math.random();
						if (random3<0.90) {
							this.pause++;
							break;
						}
					}
				}
			}
			 double random = Math.random();
				if (random<0.60) {
					double random2 = Math.random();
					int indice = (int) (random2*attori.size());
					Actor a = attori.get(indice);
					attori.remove(a);
					intervistati.add(a);
				}else {
					Actor precendente = intervistati.get(intervistati.size()-1);
					Actor a = trovaRaccomandato(precendente, attori);
					attori.remove(a);
					intervistati.add(a);
				}
			
		}
	}

	private Actor trovaRaccomandato(Actor precendente, List<Actor> attori) {
		int max =0;
		Actor a = null;
		List<Actor> gradoMax = new ArrayList<>();
		List<Actor> vicini = Graphs.neighborListOf(this.grafo, precendente);
		if (vicini.size()<1) {
			double random2 = Math.random();
			int indice = (int) (random2*attori.size());
			a = attori.get(indice);
		}else {
			for (Actor a1 : vicini) {
				DefaultWeightedEdge e = this.grafo.getEdge(precendente, a1);
				double peso = this.grafo.getEdgeWeight(e);
				if (peso>=max) {
					max = (int) peso;
				}
			}
			for (Actor a1 : vicini) {
				DefaultWeightedEdge e = this.grafo.getEdge(precendente, a1);
				double peso = this.grafo.getEdgeWeight(e);
				if (peso==max) {
					gradoMax.add(a1);
				}
			}
			
			if (gradoMax.size()==1) {
				a = gradoMax.get(0);
			}else {
				double random2 = Math.random();
				int indice = (int) (random2*gradoMax.size());
				a = gradoMax.get(indice);
				
			}
		}
		if (intervistati.contains(a)) {
			double random2 = Math.random();
			int indice = (int) (random2*attori.size());
			a = attori.get(indice);
			return a;
		}else {
			return a;
		}
	}
	public int getpause() {
		return this.pause;
		
	}
	public List<Actor> getIntervistati(){
		return this.intervistati;
	}
	
	
	

}
