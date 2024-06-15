// maison.reducer.ts

import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IMaison, defaultValue } from 'app/shared/model/maison.model';
import { loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';

const initialState: EntityState<IMaison> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/maisons';

// Actions

export const getEntities = createAsyncThunk('maison/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IMaison[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'maison/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IMaison>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'maison/create_entity',
  async ({ entity, imagesPieces, prix }: { entity: IMaison; imagesPieces: string[]; prix: number }, thunkAPI) => {
    const result = await axios.post<IMaison>(apiUrl, cleanEntity(entity));
    const maisonId = result.data.id;
    // Ajout des pièces
    const pieceRequests = imagesPieces.map(image =>
      axios.post('api/pieces', {
        libelle: `Piece for maison ${maisonId}`,
        image,
        etat: 'NONLOUE',
        maison_id:`${maisonId}`,
      })
    );
    await Promise.all(pieceRequests);
    // Ajout de la location
    await axios.post('api/locations', {
      prix,
      maison_id: `${maisonId}`
    });
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'maison/update_entity',
  async ({ entity, imagesPieces, prix }: { entity: IMaison; imagesPieces: string[]; prix: number }, thunkAPI) => {
    const result = await axios.put<IMaison>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    const maisonId = result.data.id;
    // Ajout des pièces
    const pieceRequests = imagesPieces.map(image =>
      axios.post('api/pieces', {
        libelle: `Piece for maison ${maisonId}`,
        image,
        etat: 'NONLOUE',
        maison_id: `${maisonId}`,
      })
    );
    await Promise.all(pieceRequests);
    // Ajout de la location
    await axios.post('api/locations', {
      prix,
      maison_id: `${maisonId}`,
    });
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'maison/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IMaison>(requestUrl);
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const MaisonSlice = createEntitySlice({
  name: 'maison',
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
        state.loading = false;
        state.links = parseHeaderForLinks(action.payload.headers.link);
        state.totalItems = action.payload.headers['x-total-count'];
        state.entities = loadMoreDataWhenScrolled(state.entities, action.payload.data, state.links);
      })
      .addMatcher(isFulfilled(createEntity, updateEntity), (state, action) => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, deleteEntity), state => {
        state.updating = true;
      });
  },
});

export const { reset } = MaisonSlice.actions;

// Reducer
export default MaisonSlice.reducer;
