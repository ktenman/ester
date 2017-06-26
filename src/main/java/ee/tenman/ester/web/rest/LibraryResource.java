package ee.tenman.ester.web.rest;

import com.codahale.metrics.annotation.Timed;
import ee.tenman.ester.service.LibraryService;
import ee.tenman.ester.web.rest.util.HeaderUtil;
import ee.tenman.ester.web.rest.util.PaginationUtil;
import ee.tenman.ester.service.dto.LibraryDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Library.
 */
@RestController
@RequestMapping("/api")
public class LibraryResource {

    private final Logger log = LoggerFactory.getLogger(LibraryResource.class);

    private static final String ENTITY_NAME = "library";

    private final LibraryService libraryService;

    public LibraryResource(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * POST  /libraries : Create a new library.
     *
     * @param libraryDTO the libraryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new libraryDTO, or with status 400 (Bad Request) if the library has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/libraries")
    @Timed
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) throws URISyntaxException {
        log.debug("REST request to save Library : {}", libraryDTO);
        if (libraryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new library cannot already have an ID")).body(null);
        }
        LibraryDTO result = libraryService.save(libraryDTO);
        return ResponseEntity.created(new URI("/api/libraries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /libraries : Updates an existing library.
     *
     * @param libraryDTO the libraryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated libraryDTO,
     * or with status 400 (Bad Request) if the libraryDTO is not valid,
     * or with status 500 (Internal Server Error) if the libraryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/libraries")
    @Timed
    public ResponseEntity<LibraryDTO> updateLibrary(@Valid @RequestBody LibraryDTO libraryDTO) throws URISyntaxException {
        log.debug("REST request to update Library : {}", libraryDTO);
        if (libraryDTO.getId() == null) {
            return createLibrary(libraryDTO);
        }
        LibraryDTO result = libraryService.save(libraryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, libraryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /libraries : get all the libraries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of libraries in body
     */
    @GetMapping("/libraries")
    @Timed
    public ResponseEntity<List<LibraryDTO>> getAllLibraries(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Libraries");
        Page<LibraryDTO> page = libraryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/libraries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /libraries/:id : get the "id" library.
     *
     * @param id the id of the libraryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the libraryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/libraries/{id}")
    @Timed
    public ResponseEntity<LibraryDTO> getLibrary(@PathVariable Long id) {
        log.debug("REST request to get Library : {}", id);
        LibraryDTO libraryDTO = libraryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(libraryDTO));
    }

    /**
     * DELETE  /libraries/:id : delete the "id" library.
     *
     * @param id the id of the libraryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/libraries/{id}")
    @Timed
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        log.debug("REST request to delete Library : {}", id);
        libraryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
