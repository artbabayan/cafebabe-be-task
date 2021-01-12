package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.OperationFailedException;
import com.babayan.babe.cafe.app.exceptions.ResourceAlreadyInUseException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.model.dao.TableEntity;
import com.babayan.babe.cafe.app.model.dto.Table;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.TableStatusEnum;
import com.babayan.babe.cafe.app.repository.TableRepository;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.TableService;
import com.babayan.babe.cafe.app.service.UserService;
import com.babayan.babe.cafe.app.util.IntegerHelper;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class TableServiceImpl implements TableService {
    private TableRepository tableRepository;
    @Autowired public void setTableRepository(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <TABLE SERVICE>

    @PostConstruct
    private void init() {
        log.info("Table service initialized");
    }

    /**
     * @see TableService#createTable(Table)
     */
    @Transactional
    @Override
    public Table createTable(Table table) {
        if (table.getId() != null) {
            throw new ValidationException(String.format("Table id can not be non null, id=[%s]", table.getId()));
        }

        checkTableExistByTableNumber(table.getTableNumber());
        table.setOwner(null);

        TableEntity tableEntity = mapper.map(table, TableEntity.class);
        // save/store user entity
        tableRepository.saveAndFlush(tableEntity);
        log.info(String.format("Table successfully created, id [%s]", table.getId()));

        return mapper.map(tableEntity, Table.class);
    }

    /**
     * @see TableService#findById(Long)
     */
    @Override
    public Table findById(Long id) {
        Optional<TableEntity> optional = tableRepository.findById(id);
        if (!optional.isPresent()){
            log.error(String.format("Table with id [%s] not found.", id));
            throw new NotFoundException(messageService.getMessage("error.table.not.found"));
        }
        log.info(String.format("Table with id [%s] successfully fetched", id));
        return mapper.map(optional.get(), Table.class);
    }

    /**
     * @see TableService#findByUserId(Long)
     */
    @Override
    public Table findByUserId(Long userId) {
        TableEntity entity = tableRepository.findByOwnerId(userId);
        if (entity == null){
            log.error(String.format("Table with owner id [%s] not found.", userId));
            throw new NotFoundException(messageService.getMessage("error.table.not.found"));
        }
        log.info(String.format("Table with owner id [%s] successfully fetched", userId));
        return mapper.map(entity, Table.class);
    }

    /**
     * @see TableService#findByUserEmail(String)
     */
    @Override
    public Table findByUserEmail(String email) {
        TableEntity entity = tableRepository.findByOwnerEmail(email);
        if (entity == null){
            log.error(String.format("Table with owner email [%s] not found.", email));
            throw new NotFoundException(messageService.getMessage("error.table.not.found"));
        }
        log.info(String.format("Table with owner email [%s] successfully fetched", email));
        return mapper.map(entity, Table.class);
    }

    /**
     * @see TableService#findByTableNumber(int)
     */
    @Override
    public Table findByTableNumber(int number) {
        TableEntity entity = tableRepository.findByTableNumber(number);
        if (entity == null){
            log.error(String.format("Table with number [%s] not found.", number));
            throw new NotFoundException(messageService.getMessage("error.table.not.found"));
        }
        log.info(String.format("Table with number [%s] successfully fetched", number));
        return mapper.map(entity, Table.class);
    }

    /**
     * @see TableService#assignTableToUser(int, Long)
     */
    @Transactional
    @Override
    public Table assignTableToUser(int tableNumber, Long userId) {
        if (userId == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        Table table = findByTableNumber(tableNumber);
        checkTableIsAvailable(table);

        User user = userService.findById(userId);

        table.setOwner(user); // set new user
        table.setStatus(TableStatusEnum.OCCUPIED); //change table status

        TableEntity entity = mapper.map(table, TableEntity.class);
        // save/store user entity
        entity = tableRepository.saveAndFlush(entity);
        log.info(String.format("User with id [%s] successfully assigned to table with id [%s]", user.getId(), table.getId()));

        return mapper.map(entity, Table.class);
    }

    /**
     * @see TableService#existByTableNumber(int)
     */
    @Override
    public boolean existByTableNumber(int number) {
        return tableRepository.existsByTableNumber(number);
    }

    //endregion

    // region <HELPER>

    private void checkTableExistByTableNumber(int tableNumber) {
        if (!IntegerHelper.isPositive(tableNumber)) {
            log.error("Table number can not be zero or non-positive number, so generate new table number");
            throw new ValidationException(messageService.getMessage("validation.non.consistent.data"));
        }

        boolean exists = tableRepository.existsByTableNumber(tableNumber);
        if (exists) {
            log.info(String.format("Table with table number=[%s] exist", tableNumber));
            throw new ResourceAlreadyInUseException(messageService.getMessage("error.table.exist"));
        }
    }

    private void checkTableIsAvailable(Table table) {
        if (table.getStatus() != TableStatusEnum.AVAILABLE) {
            throw new OperationFailedException(messageService.getMessage("error.table.not.prepared"));
        }
    }

    //endregion

}
