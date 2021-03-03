// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
<style include='lumo-badge'>
        html {
      --lumo-primary-color: hsl(27, 90%, 52%);
      --lumo-primary-text-color: hsl(27, 90%, 52%);
      --lumo-size-xl: 4rem;
      --lumo-size-l: 3rem;
      --lumo-size-m: 2.5rem;
      --lumo-size-s: 2rem;
      --lumo-size-xs: 1.75rem;

    }

</style>
</custom-style>


`;

document.head.appendChild($_documentContainer.content);
