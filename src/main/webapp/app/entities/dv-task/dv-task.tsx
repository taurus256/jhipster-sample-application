import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDVTask } from 'app/shared/model/dv-task.model';
import { getEntities } from './dv-task.reducer';

export const DVTask = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const dVTaskList = useAppSelector(state => state.dVTask.entities);
  const loading = useAppSelector(state => state.dVTask.loading);
  const totalItems = useAppSelector(state => state.dVTask.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="dv-task-heading" data-cy="DVTaskHeading">
        <Translate contentKey="jhipsterSampleApplicationApp.dVTask.home.title">DV Tasks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplicationApp.dVTask.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dv-task/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplicationApp.dVTask.home.createLabel">Create new DV Task</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {dVTaskList && dVTaskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uuid')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.uuid">Uuid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createTime')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.createTime">Create Time</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('retryCounter')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.retryCounter">Retry Counter</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('errorDesc')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.errorDesc">Error Desc</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('operationSystem')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.operationSystem">Operation System</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('browser')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.browser">Browser</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('resolutionWidth')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.resolutionWidth">Resolution Width</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('resolutionHeight')}>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.resolutionHeight">Resolution Height</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.dVTask.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dVTaskList.map((dVTask, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dv-task/${dVTask.id}`} color="link" size="sm">
                      {dVTask.id}
                    </Button>
                  </td>
                  <td>{dVTask.uuid}</td>
                  <td>{dVTask.createTime ? <TextFormat type="date" value={dVTask.createTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{dVTask.retryCounter}</td>
                  <td>{dVTask.errorDesc}</td>
                  <td>
                    <Translate contentKey={`jhipsterSampleApplicationApp.OS.${dVTask.operationSystem}`} />
                  </td>
                  <td>
                    <Translate contentKey={`jhipsterSampleApplicationApp.BROWSER.${dVTask.browser}`} />
                  </td>
                  <td>{dVTask.resolutionWidth}</td>
                  <td>{dVTask.resolutionHeight}</td>
                  <td>{dVTask.user ? <Link to={`/dv-user/${dVTask.user.id}`}>{dVTask.user.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/dv-task/${dVTask.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/dv-task/${dVTask.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/dv-task/${dVTask.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="jhipsterSampleApplicationApp.dVTask.home.notFound">No DV Tasks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={dVTaskList && dVTaskList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default DVTask;
