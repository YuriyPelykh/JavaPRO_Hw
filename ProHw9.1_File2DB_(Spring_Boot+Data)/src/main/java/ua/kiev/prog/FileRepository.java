package ua.kiev.prog;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<LoadedFile, Long> {
    @Query("SELECT f FROM LoadedFile f WHERE LOWER(f.filename) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<LoadedFile> findByPattern(@Param("pattern") String pattern, Pageable pageable);
}
