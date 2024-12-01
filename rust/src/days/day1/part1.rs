use std::fs;

pub fn run() {
    let input =
        fs::read_to_string("inputs/day1/input").expect("should have been able to read input");
    let split = input.split_whitespace();
    let mut left_numbers: Vec<u32> = vec![];
    let mut right_numbers: Vec<u32> = vec![];
    for (index, number) in split.enumerate() {
        let number = number.parse().expect("line contained invalid number");
        if index % 2 == 0 {
            left_numbers.push(number);
        } else {
            right_numbers.push(number);
        }
    }
    left_numbers.sort();
    right_numbers.sort();
    let sum: u32 = left_numbers
        .iter()
        .zip(right_numbers.iter())
        .map(|(left_number, right_number)| {
            return left_number.abs_diff(*right_number);
        })
        .sum();
    println!("sum of differences is {sum}");
}
