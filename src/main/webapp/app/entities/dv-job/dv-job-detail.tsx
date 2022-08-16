import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dv-job.reducer';

export const DVJobDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dVJobEntity = useAppSelector(state => state.dVJob.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dVJobDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.dVJob.detail.title">DVJob</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="jhipsterSampleApplicationApp.dVJob.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.uuid}</dd>
          <dt>
            <span id="createTime">
              <Translate contentKey="jhipsterSampleApplicationApp.dVJob.createTime">Create Time</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.createTime ? <TextFormat value={dVJobEntity.createTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isError">
              <Translate contentKey="jhipsterSampleApplicationApp.dVJob.isError">Is Error</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.isError ? 'true' : 'false'}</dd>
          <dt>
            <span id="isSuccess">
              <Translate contentKey="jhipsterSampleApplicationApp.dVJob.isSuccess">Is Success</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.isSuccess ? 'true' : 'false'}</dd>
          <dt>
            <span id="operationSystem">
              <Translate contentKey="jhipsterSampleApplicationApp.dVJob.operationSystem">Operation System</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.operationSystem}</dd>
          <dt>
            <span id="browser">
              <Translate contentKey="jhipsterSampleApplicationApp.dVJob.browser">Browser</Translate>
            </span>
          </dt>
          <dd>{dVJobEntity.browser}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.dVJob.task">Task</Translate>
          </dt>
          <dd>{dVJobEntity.task ? dVJobEntity.task.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/dv-job" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dv-job/${dVJobEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DVJobDetail;
