package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private List<String> generi;
	private Map<Integer, Actor> mappa;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.generi = dao.getGeneres();
		this.mappa = new HashMap<>();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public List<String> getGeneri(){
		return this.generi;
	}
	
	public void creaGrafo(String g) {
		for (Actor a : dao.listAllActors()) {
			mappa.put(a.id, a);
		}
		Graphs.addAllVertices(this.grafo, dao.getAttori(g, mappa));
		List<Arco> archi = dao.getArchi(g, mappa);
		for (Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	public int getV() {
		return this.grafo.vertexSet().size();
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}
	public List<Actor> getVertici(){
		List<Actor> a = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(a);
		return a ;
	}
	
	public List<Actor> simili(Actor a){
		BreadthFirstIterator<Actor, DefaultWeightedEdge> visita = new BreadthFirstIterator<>(this.grafo,a);
		List<Actor> raggiungibili = new ArrayList<>();
		while (visita.hasNext()) {
			Actor a1=  visita.next();
			raggiungibili.add(a1);
		}
		raggiungibili.remove(a);
		Collections.sort(raggiungibili);
		return raggiungibili;
	}
	
	
	
	
	

}
