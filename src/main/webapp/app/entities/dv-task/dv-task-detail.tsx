import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dv-task.reducer';

export const DVTaskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dVTaskEntity = useAppSelector(state => state.dVTask.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dVTaskDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.dVTask.detail.title">DVTask</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.uuid}</dd>
          <dt>
            <span id="createTime">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.createTime">Create Time</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.createTime ? <TextFormat value={dVTaskEntity.createTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="retryCounter">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.retryCounter">Retry Counter</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.retryCounter}</dd>
          <dt>
            <span id="errorDesc">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.errorDesc">Error Desc</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.errorDesc}</dd>
          <dt>
            <span id="operationSystem">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.operationSystem">Operation System</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.operationSystem}</dd>
          <dt>
            <span id="browser">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.browser">Browser</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.browser}</dd>
          <dt>
            <span id="resolutionWidth">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.resolutionWidth">Resolution Width</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.resolutionWidth}</dd>
          <dt>
            <span id="resolutionHeight">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.resolutionHeight">Resolution Height</Translate>
            </span>
          </dt>
          <dd>{dVTaskEntity.resolutionHeight}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.dVTask.user">User</Translate>
          </dt>
          <dd>{dVTaskEntity.user ? dVTaskEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/dv-task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dv-task/${dVTaskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DVTaskDetail;
