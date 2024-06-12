import React from 'react';
import { Card, CardContent, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

interface HouseProps {
  id: number;
  name: string;
  description: string;
  image: string;
}

const House: React.FC<HouseProps> = ({ id, name, description, image }) => {
  return (
    <Card sx={{ maxWidth: 345 }}>
      <CardContent>
        <Typography gutterBottom variant="h5" component="div">
          {name}
        </Typography>
        <img src={image} alt={name} style={{ width: '100%' }} />
        <Typography variant="body2" color="text.secondary">
          {description}
        </Typography>
        <Button component={Link} to={`/houses/${id}`} variant="contained" size="small">
          View Details
        </Button>
      </CardContent>
    </Card>
  );
};

export default House;
