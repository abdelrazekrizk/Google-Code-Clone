<h1>Zone view: <?php echo $zonename ?></h1>

<table>
  <thead>
    <tr>
      <th>Boss Name</th>
      <th>Base</th>
      <th>Kill Count</th>
    </tr>
  </thead>
  <tbody>
  <?php foreach($bosses as $boss): ?>
    <tr>
      <td><?php $boss->echoLink(); ?></td>
      <td><?php echo $boss->getBaseVal(); ?></td>
      <td><?php echo $boss->getKillCount(); ?></td>
    </tr>
  <?php endforeach; ?>
  </tbody>
</table>