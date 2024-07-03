package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getGeneres(){
		String sql ="SELECT DISTINCT m.genre "
				+ "FROM movies_genres m";
		List<String> result = new ArrayList<>();
	
		Connection conn = DBConnect.getConnection();
	
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
	
				String s = res.getString("genre");	
				result.add(s);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getAttori(String g, Map<Integer,Actor>mappa){
		String sql = "SELECT distinct r.actor_id as id "
				+ "FROM roles r, movies_genres mg "
				+ "WHERE r.movie_id = mg.movie_id AND mg.genre = ? ";
		List<Actor> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			while (res.next()) {
	
				result.add(mappa.get(res.getInt("id")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Arco> getArchi(String g, Map<Integer,Actor>mappa){
		String sql = "SELECT r1.actor_id AS id1, r2.actor_id AS id2, COUNT(DISTINCT r1.movie_id) AS peso "
				+ "FROM roles r1, roles r2, movies_genres mg1, movies_genres mg2 "
				+ "WHERE mg1.genre = ? AND mg2.genre = ? AND r1.actor_id>r2.actor_id   AND r1.movie_id = mg1.movie_id "
				+ "	AND r2.movie_id = mg2.movie_id AND r1.movie_id = r2.movie_id "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g);
			st.setString(2, g);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Arco a = new Arco(mappa.get(res.getInt("id1")), mappa.get(res.getInt("id2")), res.getInt("peso"));
				result.add(a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
