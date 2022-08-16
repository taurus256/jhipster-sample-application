import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDVUser } from 'app/shared/model/dv-user.model';
import { getEntities as getDVUsers } from 'app/entities/dv-user/dv-user.reducer';
import { IDVTask } from 'app/shared/model/dv-task.model';
import { OS } from 'app/shared/model/enumerations/os.model';
import { BROWSER } from 'app/shared/model/enumerations/browser.model';
import { getEntity, updateEntity, createEntity, reset } from './dv-task.reducer';

export const DVTaskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dVUsers = useAppSelector(state => state.dVUser.entities);
  const dVTaskEntity = useAppSelector(state => state.dVTask.entity);
  const loading = useAppSelector(state => state.dVTask.loading);
  const updating = useAppSelector(state => state.dVTask.updating);
  const updateSuccess = useAppSelector(state => state.dVTask.updateSuccess);
  const oSValues = Object.keys(OS);
  const bROWSERValues = Object.keys(BROWSER);

  const handleClose = () => {
    navigate('/dv-task' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDVUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTime = convertDateTimeToServer(values.createTime);

    const entity = {
      ...dVTaskEntity,
      ...values,
      user: dVUsers.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createTime: displayDefaultDateTime(),
        }
      : {
          operationSystem: 'MAC',
          browser: 'FIREFOX',
          ...dVTaskEntity,
          createTime: convertDateTimeFromServer(dVTaskEntity.createTime),
          user: dVTaskEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.dVTask.home.createOrEditLabel" data-cy="DVTaskCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.dVTask.home.createOrEditLabel">Create or edit a DVTask</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="dv-task-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.uuid')}
                id="dv-task-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.createTime')}
                id="dv-task-createTime"
                name="createTime"
                data-cy="createTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.retryCounter')}
                id="dv-task-retryCounter"
                name="retryCounter"
                data-cy="retryCounter"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.errorDesc')}
                id="dv-task-errorDesc"
                name="errorDesc"
                data-cy="errorDesc"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.operationSystem')}
                id="dv-task-operationSystem"
                name="operationSystem"
                data-cy="operationSystem"
                type="select"
              >
                {oSValues.map(oS => (
                  <option value={oS} key={oS}>
                    {translate('jhipsterSampleApplicationApp.OS.' + oS)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.browser')}
                id="dv-task-browser"
                name="browser"
                data-cy="browser"
                type="select"
              >
                {bROWSERValues.map(bROWSER => (
                  <option value={bROWSER} key={bROWSER}>
                    {translate('jhipsterSampleApplicationApp.BROWSER.' + bROWSER)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.resolutionWidth')}
                id="dv-task-resolutionWidth"
                name="resolutionWidth"
                data-cy="resolutionWidth"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVTask.resolutionHeight')}
                id="dv-task-resolutionHeight"
                name="resolutionHeight"
                data-cy="resolutionHeight"
                type="text"
              />
              <ValidatedField
                id="dv-task-user"
                name="user"
                data-cy="user"
                label={translate('jhipsterSampleApplicationApp.dVTask.user')}
                type="select"
              >
                <option value="" key="0" />
                {dVUsers
                  ? dVUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dv-task" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DVTaskUpdate;
