module Palet ( Palet, newP, destinationP, netP )
  where

data Palet = Pal String Int deriving (Eq, Show)

newP :: String -> Int -> Palet   -- construye un Palet dada una ciudad de destino y un peso en toneladas
destinationP :: Palet -> String  -- responde la ciudad destino del palet
netP :: Palet -> Int             -- responde el peso en toneladas del palet


newP dest weight | weight > 0 = Pal dest weight
                 | otherwise  = error "The palet can't have a negative weight"

destinationP (Pal dest _) = dest

netP (Pal _ weight) = weight


palet1 :: Palet
palet1 = newP "Buenos Aires" 10

-- main :: IO()
-- main = do
--   let palet1 = newP "Buenos Aires" 10
--   print(destinationP palet1)
--   print(netP palet1)