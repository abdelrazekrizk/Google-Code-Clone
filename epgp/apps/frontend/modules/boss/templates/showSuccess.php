<h1>Boss view</h1>
<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Zone</th>
      <th>Base Value</th>
      <th>Kill count</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><?php $bossinfo->echoLink(); ?></td>
      <td><?php $bossinfo->getZone()->echoLink(); ?></td>
      <td><?php echo $bossinfo->getBaseVal(); ?></td>
      <td><?php echo $killcount ?></td>
    </tr>
  </tbody>
</table>

<h3>Raid Listing</h3>
<table>
  <thead>
    <tr>
      <th>Date</th>
      <th>Note</th>
      <th>Base</th>
      <th>Inflated</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($raids as $raid): ?>
    <tr>
      <td><?php $raid->echoMdyLink(); ?></td>
      <td><?php $raid->echoNoteLink(); ?></td>
      <td><?php echo $raid->getBaseVal(); ?></td>
      <td><?php echo $raid->getInflatedVal(); ?></td>
    </tr>
   <?php endforeach; ?>
  </tbody>
</table>

<h3>Item Listing</h3>
<table>
  <thead>
    <tr>
      <th>Date</th>
      <th>Item</th>
      <th>Awarded to</th>
      <th>Base</th>
      <th>Inflated</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($items as $item): ?>
    <tr>
      <td><?php $item->getRaid()->echoMdyLink(); ?></td>
      <td><?php $item->echoLink(); ?></td>
      <td><?php $item->getAwardee()->echoLink(); ?></td>
      <td><?php echo $item->getBaseVal(); ?></td>
      <td><?php echo $item->getInflatedVal(); ?></td>
    </tr>
  <?php endforeach; ?>
  </tbody>
</table>