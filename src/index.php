<?php
  include("includes/header.php")
?>

  <main id="contenu">
    <section class="hero">
      <div class="hero-text">
        <p class="label">Collection officielle / The Legend of Zelda</p>
        <h1>L’aventure ne s’arrête pas.</h1>
        <p class="hero-sub">Vos univers prennent une autre dimension.</p>
        <a class="btn" href="catalogue.php?licence=zelda">Explorer la collection Zelda
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true"><path d="M7 17L17 7M8 7h9v9"/></svg>
        </a>
      </div>
      <img class="hero-img" src="assets/hero-zelda.jpg" width="1200" height="600" alt="Figurines amiibo Link de The Legend of Zelda" fetchpriority="high">
    </section>

    <section class="licences" id="licences" aria-label="Licences">
      <div class="wrap licences-inner">
        <p class="label">Les univers<br>qui nous animent</p>
        <ul>
          <li><a href="catalogue.php?licence=zelda">Zelda</a></li>
          <li><a href="catalogue.php?licence=pokemon">Pokémon</a></li>
          <li><a href="catalogue.php?licence=super-mario">Super Mario</a></li>
          <li><a href="catalogue.php?licence=sonic">Sonic</a></li>
          <li><a href="catalogue.php?licence=final-fantasy">Final Fantasy</a></li>
        </ul>
        <a class="link-more" href="catalogue.php">Tout voir ↗</a>
      </div>
    </section>

    <section class="wrap section" id="nouveautes">
      <div class="section-head">
        <h2>Les articles du moment</h2>
        <a class="link-more" href="catalogue.php">Toute la sélection ↗</a>
      </div>
      <div class="products">
        <article class="product">
          <span class="badge">Coup de cœur</span>
          <button class="wish" type="button" aria-label="Ajouter LEGO Luigi à ma liste de souhaits">♡</button>
          <img src="assets/lego-luigi.webp" width="400" height="400" loading="lazy" alt="Figurine LEGO Luigi">
          <p class="product-licence">Super Mario</p>
          <h3><a href="produit.php?id=1">LEGO Luigi</a></h3>
          <p class="price">35,90 €</p>
        </article>
        <article class="product">
          <span class="badge">Best-seller</span>
          <button class="wish" type="button" aria-label="Ajouter Pikachu à ma liste de souhaits">♡</button>
          <img src="assets/funko-pikachu.jpg" width="400" height="400" loading="lazy" alt="Figurine Funko Pop Pikachu">
          <p class="product-licence">Pokémon</p>
          <h3><a href="produit.php?id=2">FUNKO Figurine POP Pikachu</a></h3>
          <p class="price">19,90 €</p>
        </article>
        <article class="product">
          <button class="wish" type="button" aria-label="Ajouter la décoration Loot Llama à ma liste de souhaits">♡</button>
          <img src="assets/llama-fortnite.jpg" width="400" height="400" loading="lazy" alt="Décoration de sapin Loot Llama Fortnite">
          <p class="product-licence">Fortnite</p>
          <h3><a href="produit.php?id=3">Décoration Sapin – Loot Llama Fortnite</a></h3>
          <p class="price">24,90 €</p>
        </article>
        <article class="product">
          <span class="badge">Nouveau</span>
          <button class="wish" type="button" aria-label="Ajouter la figurine Geralt de Riv à ma liste de souhaits">♡</button>
          <img src="assets/geralt.jpg" width="400" height="400" loading="lazy" alt="Figurine Geralt de Riv, The Witcher">
          <p class="product-licence">The Witcher</p>
          <h3><a href="produit.php?id=4">Figurine Geralt de Riv</a></h3>
          <p class="price">79,90 €</p>
        </article>
      </div>
    </section>

    <section class="wrap section" id="categories">
      <div class="section-head">
        <h2>Catégories</h2>
        <p class="label">Choisissez votre terrain de jeu</p>
      </div>
      <div class="categories">
        <a class="category" href="catalogue.php?categorie=vetements">
          <img src="assets/cat-vetements.jpg" width="600" height="400" loading="lazy" alt="">
          <span>Vêtements</span>
        </a>
        <a class="category" href="catalogue.php?categorie=figurines">
          <img src="assets/cat-figurines.jpg" width="600" height="400" loading="lazy" alt="">
          <span>Figurines</span>
        </a>
        <a class="category" href="catalogue.php?categorie=accessoires">
          <img src="assets/cat-accessoires.png" width="600" height="400" loading="lazy" alt="">
          <span>Accessoires</span>
        </a>
      </div>
    </section>

    <section class="wrap reviews" aria-label="Avis clients">
      <div class="rating">
        <p class="rating-score"><span class="stars" aria-hidden="true">★★★★★</span> 4,8/5.</p>
        <p class="muted">Plus de 1 200 avis.</p>
      </div>
      <blockquote class="review">
        <p class="stars" aria-label="5 étoiles sur 5">★★★★★</p>
        <p>La figurine est encore plus belle en vrai. Emballage impeccable, reçue en deux jours.</p>
        <footer>Camille R. · Achat vérifié</footer>
      </blockquote>
      <blockquote class="review">
        <p class="stars" aria-label="5 étoiles sur 5">★★★★★</p>
        <p>Enfin une sélection qui a du goût. Le hoodie Ectoplasma ne me quitte plus.</p>
        <footer>Thomas K. · Achat vérifié</footer>
      </blockquote>
    </section>

    <section class="perks" aria-label="Nos engagements">
      <ul class="wrap perks-inner">
        <li><strong>Expédition sous 48 h</strong><span>Livraison offerte à partir de 60 €.</span></li>
        <li><strong>Retours sous 30 jours</strong><span>Changez d’avis, on s’occupe du reste.</span></li>
        <li><strong>Paiement sécurisé</strong><span>CB et PayPal.</span></li>
      </ul>
    </section>

    <section class="newsletter">
      <div class="wrap newsletter-inner">
        <h2>S’abonner à la newsletter</h2>
        <form action="newsletter.php" method="post">
          <label class="sr-only" for="email">Votre adresse e-mail</label>
          <div class="field">
            <input id="email" name="email" type="email" placeholder="Votre adresse e-mail" required autocomplete="email">
            <button type="submit" aria-label="S’abonner">↗</button>
          </div>
          <p class="muted small">En vous inscrivant, vous acceptez notre politique de confidentialité.</p>
        </form>
      </div>
    </section>
  </main>

<?php
  include("includes/footer.php")
?>