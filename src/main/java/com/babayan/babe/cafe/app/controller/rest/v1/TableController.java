package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.model.dto.Table;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.service.TableService;
import com.babayan.babe.cafe.app.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author artbabayan
 */
@Validated
@Log4j2
@RestController
@RequestMapping(value = "/api/v1/tables", produces = MediaType.APPLICATION_JSON_VALUE)
public class TableController {
    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private TableService tableService;
    @Autowired public void setTableService(TableService tableService) {
        this.tableService = tableService;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    /**
     * Initializes a new instance of the class
     */
    public TableController() {
        log.info("Tables API initialized");
    }

    // region <TABLE CONTROLLER>

    /**
     * Create new cafe table
     */
    @PostMapping
    public ResponseEntity<Table> createTable(@RequestBody @Valid Table table) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Table newTable = tableService.createTable(table);
        return new ResponseEntity<>(newTable, HttpStatus.OK);
    }

    /**
     * Gets table by specific user
     */
    @GetMapping("/user-table")
    public ResponseEntity<Table> findTableByUserId() {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        User user = userService.findById(currentUserID);
        Table newTable = tableService.findByUserEmail(user.getEmail());
        return new ResponseEntity<>(newTable, HttpStatus.OK);
    }

    /**
     * Gets table by specific table id
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Table> findById(@PathVariable("id") long id) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Table table = tableService.findById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    /**
     * Gets table by specific table number
     */
    @GetMapping("/{number}")
    public ResponseEntity<Table> findByTableNumber(@PathVariable("number") int number) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Table newTable = tableService.findByTableNumber(number);
        return new ResponseEntity<>(newTable, HttpStatus.OK);
    }

    /**
     * Assign new user to available table
     */
    @PutMapping("/assign-user")
    public ResponseEntity<Table> assignTableToUser(@RequestParam(value = "tableNmber") int tableNumber,
                                                   @RequestParam(value = "userId") long userId) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Table newTable = tableService.assignTableToUser(tableNumber, userId);
        return new ResponseEntity<>(newTable, HttpStatus.OK);
    }

    //endregion

}
