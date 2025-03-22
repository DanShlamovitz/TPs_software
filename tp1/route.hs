module Route ( Route, newR, inOrderR, inRouteR, getRoute)
   where

data Route = Rou [ String ] deriving (Eq, Show)

newR :: [ String ] -> Route -- construye una ruta según una lista de ciudades
inOrderR :: Route -> String -> String -> Bool -- indica si la primer ciudad consultada está antes que la segunda ciudad en la ruta
inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta
getRoute :: Route -> [ String ]

newR cities | null(cities) == True = error "The city list has to contain at least one city"
            | otherwise = Rou cities

inRouteR (Rou route) c1 | null(route) == True = False
                        | route !! 0 == c1 = True
                        | otherwise = inRouteR (Rou (tail route)) c1

-- Tal vez en inOrderR haya que devolver error o False si alguna de las dos (una o la otra) no esta en la ruta
inOrderR (Rou route) c1 c2 | inRouteR (Rou route) c1 == False = error ("The city " ++ c1 ++ " isn't a part of the provided route")
                           | inRouteR (Rou route) c2 == False = error ("The city " ++ c2 ++ " isn't a part of the provided route")
                           | route !! 0 == c1 = True
                           | route !! 0 == c2 = False
                           | otherwise = inOrderR (Rou (tail route)) c1 c2
getRoute (Rou route) = route