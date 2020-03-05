// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomQuote() {
  const random_quote =
      ["Please note that we have added a consequence for failure. Any contact with the chamber floor will result in an 'unsatisfactory'  mark on your official testing record, followed by death. Good luck!", 
      "You're doing very well. Please be advised that a noticeable taste of blood is not part of any test protocol but is an unintended side effect of the Aperture Science Material Emancipation Grill, which may, in semi-rare cases, emancipate dental fillings, crowns, tooth enamel, and teeth.", 
      "What are you doing? Stop it! I-i-I-iiiiiiiiiiiiiiiiii... We are pleased that you made it through the final challenge where we pretended we were going to murder you. We are very, very happy for your success. We are throwing a party in honour of your tremendous success. Place the device on the ground, then lie on your stomach with your arms at your sides. A party associate will arrive shortly to collect you for your party. Make no further attempt to leave the testing area. Assume the party escort submission position or you will miss the party.", 
      "Momentum, a function of mass and velocity, is conserved between portals. In layman's terms: speedy thing goes in, speedy thing comes out.",
      "Spectacular. You appear to understand how a portal affects forward momentum, or to be more precise, how it does not."];

  // Pick a random greeting.
  const quote = random_quote[Math.floor(Math.random() * random_quote.length)];

  // Add it to the page.
  const quote_container = document.getElementById('quote-container');
  quote_container.innerText = quote;
}

function fetchListData(){
    fetch("/data").then(response => response.text()).then((data) => {
        
        const data_container = document.getElementById("list-data-container");
        data_container.innerText = data;

    });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
