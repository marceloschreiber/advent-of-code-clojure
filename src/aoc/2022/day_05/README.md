# Day 5 (2022)
`Supply Stacks` ([prompt](https://adventofcode.com/2022/day/5))

---

## ChatGTP

I solved the problem myself. Then I decided to play around with ChatGTP and had this conversation:

```
[Pasted my Clojure solution here]
Me: In human terms, what is the code above doing?

ChatGPT:
The code above is a solution to Advent of Code's 2022 Day 05 puzzle. It is written in the Clojure programming language.

The code defines several functions that are used to solve the puzzle. The transpose function takes a matrix as input and returns its transpose. The extract-crates function takes a string that represents the layout of crates in a warehouse and returns a vector of keywords representing the crates in each column. The pille-crates function takes a matrix of crates and returns a matrix of the same crates, but with the crates in each column transposed to the corresponding row. The extract-rearrangement function takes a string that represents a rearrangement of crates in the warehouse and returns a map containing the source and destination indices and the quantity of crates to be moved.

The input function uses these helper functions to parse the input data for the puzzle. It reads the input file, which contains the layout of the crates in the warehouse and the rearrangement instructions, and returns a map with two keys: :crates, which contains the matrix of crates, and :rearrangements, which contains the sequence of rearrangement instructions.

The crate-mover-9000 and crate-mover-9001 functions are used to move crates from one location to another in the warehouse. The crate-mover-9000 function simply takes the specified number of crates from the source location, while the crate-mover-9001 function takes the specified number of crates from the source location in reverse order.

The move-crates-with-crate-mover function uses the crate-mover function to move crates from one location to another in the warehouse. It takes the current state of the warehouse, a rearrangement instruction, and a crate-mover function as input, and returns the updated state of the warehouse after the specified crates have been moved.

The rearrange-crates-with-crate-mover function uses the move-crates-with-crate-mover function to apply a sequence of rearrangement instructions to the warehouse. It takes the initial state of the warehouse, a sequence of rearrangement instructions, and a crate-mover function as input, and returns the final state of the warehouse after all the rearrangements have been applied.

The join-top-crates function takes the final state of the warehouse as input and returns a string representation of the crates on the top row of the warehouse.

The part-1 and part-2 functions use the rearrange-crates-with-crate-mover and join-top-crates functions to solve the puzzle. The part-1 function uses the crate-mover-9000 function to move the crates, while the part-2 function uses the crate-mover-9001 function. Both functions take the parsed input data as input and return the string representation of the crates on the top row of the warehouse after all the rearrangements have been applied. The main function prints the solutions for both parts of the puzzle.
```


This is amazing! It is aware that there is something called Advent of Code, that is a solution for that, and that I used the Clojure programming language. Then it proceeds to explains the code better than I would.

I also asked for it to improve my code:

```
Me: Improve the above code
```

```Chat:GPT
Here is the version of the code with some improvements:
[[Code here]]
```

It wasn't able to generate the whole file. It cropped at the middle. But it did suggest an improvement:

```clojure
;; My Code
(defn extract-rearrangement
  [rearrangement-str]
  (let [[_match, quantity, from, to] (re-find #"move (\d+) from (\d+) to (\d+)" rearrangement-str)]
    {:from     (parse-long from)
     :to       (parse-long to)
     :quantity (parse-long quantity)}))

;; ChatGPT
(defn extract-rearrangement
  [rearrangement-str]
  (let [[_match, quantity, from, to] (re-find #"move (\d+) from (\d+) to (\d+)" rearrangement-str)]
    {:from     (dec (parse-long from))
     :to       (dec (parse-long to))
     :quantity (parse-long quantity)}))
```

1. Only 1 suggestion! I can that was such a perfect code that improvements are not possible /s
2. I don't agree with the improvement. That function only goal is to extract the rearrangement from a string into a map. Without knowing anything about how this information is going to be used. To decrement there means the function knows that these numbers are going to be used as indexes for something.
