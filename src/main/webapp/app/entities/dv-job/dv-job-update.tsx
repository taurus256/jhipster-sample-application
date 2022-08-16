import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDVTask } from 'app/shared/model/dv-task.model';
import { getEntities as getDVTasks } from 'app/entities/dv-task/dv-task.reducer';
import { IDVJob } from 'app/shared/model/dv-job.model';
import { OS } from 'app/shared/model/enumerations/os.model';
import { BROWSER } from 'app/shared/model/enumerations/browser.model';
import { getEntity, updateEntity, createEntity, reset } from './dv-job.reducer';

export const DVJobUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dVTasks = useAppSelector(state => state.dVTask.entities);
  const dVJobEntity = useAppSelector(state => state.dVJob.entity);
  const loading = useAppSelector(state => state.dVJob.loading);
  const updating = useAppSelector(state => state.dVJob.updating);
  const updateSuccess = useAppSelector(state => state.dVJob.updateSuccess);
  const oSValues = Object.keys(OS);
  const bROWSERValues = Object.keys(BROWSER);

  const handleClose = () => {
    navigate('/dv-job');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getDVTasks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTime = convertDateTimeToServer(values.createTime);

    const entity = {
      ...dVJobEntity,
      ...values,
      task: dVTasks.find(it => it.id.toString() === values.task.toString()),
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
          ...dVJobEntity,
          createTime: convertDateTimeFromServer(dVJobEntity.createTime),
          task: dVJobEntity?.task?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.dVJob.home.createOrEditLabel" data-cy="DVJobCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.dVJob.home.createOrEditLabel">Create or edit a DVJob</Translate>
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
                  id="dv-job-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVJob.uuid')}
                id="dv-job-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVJob.createTime')}
                id="dv-job-createTime"
                name="createTime"
                data-cy="createTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVJob.isError')}
                id="dv-job-isError"
                name="isError"
                data-cy="isError"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVJob.isSuccess')}
                id="dv-job-isSuccess"
                name="isSuccess"
                data-cy="isSuccess"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.dVJob.operationSystem')}
                id="dv-job-operationSystem"
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
                label={translate('jhipsterSampleApplicationApp.dVJob.browser')}
                id="dv-job-browser"
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
                id="dv-job-task"
                name="task"
                data-cy="task"
                label={translate('jhipsterSampleApplicationApp.dVJob.task')}
                type="select"
              >
                <option value="" key="0" />
                {dVTasks
                  ? dVTasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dv-job" replace color="info">
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

export default DVJobUpdate;
