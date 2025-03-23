module Route ( Route, newR, inOrderR, inRouteR )
   where

data Route = Rou [ String ] deriving (Eq, Show)

newR :: [ String ] -> Route -- construye una ruta según una lista de ciudades
inOrderR :: Route -> String -> String -> Bool -- indica si la primer ciudad consultada está antes que la segunda ciudad en la ruta
inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta

newR cities | null(cities) == True = error "The city list has to contain at least one city"
            | otherwise = Rou cities

inRouteR (Rou []) _ = False
inRouteR (Rou (x:xs)) c1   
   | x == c1   = True
   | otherwise = inRouteR (Rou xs) c1


inOrderR (Rou []) _ _ = error "Empty route encountered during inOrderR"
inOrderR (Rou (x:xs)) c1 c2
   | not (inRouteR (Rou (x:xs)) c1) = error ("The city " ++ c1 ++ " isn't a part of the provided route")
   | not (inRouteR (Rou (x:xs)) c2) = error ("The city " ++ c2 ++ " isn't a part of the provided route")
   | x == c1 = before c1 c2 xs
   | otherwise = inOrderR (Rou xs) c1 c2

before :: String -> String -> [String] -> Bool
before _ _ [] = False
before c1 c2 (c:cities)
   | c == c1 = elem c2 cities
   | c == c2 = False
   | otherwise = before c1 c2 cities