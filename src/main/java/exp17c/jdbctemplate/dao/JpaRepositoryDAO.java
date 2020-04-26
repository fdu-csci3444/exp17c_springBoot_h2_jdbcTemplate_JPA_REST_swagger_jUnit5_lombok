/**
 * 
 */
package exp17c.jdbctemplate.dao;

import java.util.List;
import java.util.Optional;


/**
 * @author ilker
 *
 */
public interface JpaRepositoryDAO<T, ID> {
	public List<T> findAll();
	public Optional<T> findById(ID id);
	public Optional<T> findOne(T t);
	public T save(T t);
	public void deleteAll();
	public void deleteById(ID id);
	public void delete(T t);
	public boolean existsById(ID id);
	public boolean exists(T t);
	public long count();
}
