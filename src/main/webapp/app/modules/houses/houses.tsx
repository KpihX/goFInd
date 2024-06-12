import React, { useState, useEffect } from 'react';
import { Grid } from '@mui/material';
import InfiniteScroll from 'react-infinite-scroll-component';
import House from './house';

interface HouseData {
  id: number;
  name: string;
  description: string;
  image: string;
}

const Houses: React.FC = () => {
  const [houses, setHouses] = useState<HouseData[]>([]);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    // Fetch houses data and setHouses with the result
    // Example: fetchHouses().then(data => setHouses(data));
  }, []); // Empty dependency array to only run once on component mount

  const fetchMoreData = () => {
    // Fetch more houses data and append it to the existing houses state
    // Example: fetchMoreHouses().then(data => setHouses(prevHouses => [...prevHouses, ...data]));
    // Set hasMore to false when there's no more data to load
    // Example: setHasMore(false);
  };

  return (
    <InfiniteScroll
      dataLength={houses.length}
      next={fetchMoreData}
      hasMore={hasMore}
      loader={<h4>Loading...</h4>}
      endMessage={<p>No more houses to load</p>}
    >
      <Grid container spacing={2}>
        {houses.map(house => (
          <Grid item key={house.id} xs={12} sm={6} md={4}>
            <House id={house.id} name={house.name} description={house.description} image={house.image} />
          </Grid>
        ))}
      </Grid>
    </InfiniteScroll>
  );
};

export default Houses;
