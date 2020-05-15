package ua.kiev.prog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public void addFile(LoadedFile loadedFile) {
        fileRepository.save(loadedFile);
    }

    @Transactional
    public void deleteFiles(long[] idList) {
        for (long id : idList)
            fileRepository.delete(id);
    }

    @Transactional(readOnly=true)
    public List<LoadedFile> findAll(Pageable pageable) {
        return fileRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly=true)
    public List<LoadedFile> findByPattern(String pattern, Pageable pageable) {
        return fileRepository.findByPattern(pattern, pageable);
    }

    @Transactional(readOnly = true)
    public long count() {
        return fileRepository.count();
    }

    @Transactional
    public void reset() {
        fileRepository.deleteAll();
    }
}
