import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError, IQueryParamsIti } from 'app/shared/reducers/reducer.utils';
import { ITrajet, defaultValue } from 'app/shared/model/trajet.model';

const initialState: EntityState<ITrajet> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/trajets';

// Actions

export const getEntities = createAsyncThunk('trajet/fetch_entity_list', async ({ page, size, sort, search, search2 }: IQueryParamsIti) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}search=${search ? search : ''}&search2=${search2 ? search2 : ''}&cacheBuster=${new Date().getTime()}`;
  return axios.get<ITrajet[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'trajet/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITrajet>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'trajet/create_entity',
  async (entity: ITrajet, thunkAPI) => {
    return axios.post<ITrajet>(apiUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'trajet/update_entity',
  async ({ entity, motif }: { entity: ITrajet; motif: string }) => {
    // entity: ITrajet, thunkAPI) => {
    // console.log('** updateTrajet: ', entity);
    return axios.put<ITrajet>(`${apiUrl}/${entity.id}?motif=${motif}`, entity);
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'trajet/partial_update_entity',
  async (entity: ITrajet, thunkAPI) => {
    return axios.patch<ITrajet>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'trajet/delete_entity',
  async ({ id, motif }: { id: string | number; motif: string }, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}?motif=${motif}`;
    return await axios.delete<ITrajet>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntities = createAsyncThunk(
  'trajet/delete_entities',
  async ({ ids, motif }: { ids: (string | number)[]; motif: string }, thunkAPI) => {
    // console.log('* ids: ', ids);
    // console.log('* motif: ', motif);
    const requestUrl = `${apiUrl}/delete-entities?motif=${motif}`;
    return await axios.delete(requestUrl, { data: ids });
  },
  { serializeError: serializeAxiosError },
);

// slice

export const TrajetSlice = createEntitySlice({
  name: 'trajet',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;
        const links = parseHeaderForLinks(headers.link);

        return {
          ...state,
          loading: false,
          links,
          entities: loadMoreDataWhenScrolled(state.entities, data, links),
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = TrajetSlice.actions;

// Reducer
export default TrajetSlice.reducer;
